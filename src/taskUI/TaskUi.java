package taskUI;

import java.util.ArrayList;
import java.util.List;

import event.StateChangedEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import taskVerwaltung.Task;
import taskVerwaltung.Ziel;

public class TaskUi extends VBox {


    final private static String TASK_DEFAULT_NAME = "Task";
    private Button addButton = new Button("Add");
    private TreeView<Task> myTree = new TreeView<Task>();
    private List<Task> allTasks = new ArrayList<Task>();
    private TaskInfoUI taskInfoUI = new TaskInfoUI(myTree);
    public TaskUi() {
        super();
        setUp();
        onAction();
        signalChanges();
    }

    private void setUp() {
        //
        setMaxWidth(250);
        setMinWidth(250);
        // Lay
        VBox treeBox = new VBox();
        VBox.setMargin(myTree, new Insets(3));
        VBox.setMargin(addButton, new Insets(3));

        treeBox.setSpacing(4);
        setSpacing(2);
        treeBox.getChildren().addAll(myTree, addButton);
        // Die UI konfigurieren
        getChildren().addAll(treeBox, new Separator(), taskInfoUI);
        myTree.setRoot(new CheckBoxTreeItem<Task>(new Ziel("Null")));
        myTree.setShowRoot(false);
        // Dadurch lassen sich die Items bearbeiten
        myTree.setEditable(true);
        myTree.setCellFactory(new Callback<TreeView<Task>, TreeCell<Task>>() {
            @Override
            public TreeCell<Task> call(TreeView<Task> arg0) {
                // TODO Auto-generated method stub
                return new CustomTreeCell(TaskUi.this);
            }
        });
        // Add Button
        addButton.setPrefWidth(Double.MAX_VALUE);
        addButton.setOnAction(e ->
                {
                    addTreeTask(new Task(TASK_DEFAULT_NAME));
                }
        );

    }

    private void onAction() {

        myTree.setOnKeyPressed(e ->
        {
            CheckBoxTreeItem<Task> taskItem;
            if (e.getCode() == KeyCode.PLUS) {
                taskItem = (CheckBoxTreeItem<Task>) myTree.getSelectionModel().getSelectedItem();
                addItemAction(taskItem, this);
            } else if (e.getCode() == KeyCode.MINUS) {
                taskItem = (CheckBoxTreeItem<Task>) myTree.getSelectionModel().getSelectedItem();
                removeItemAction(taskItem, this);
            }
        });
    }

    public CheckBoxTreeItem<Task> getSelectedItem() {
        return (CheckBoxTreeItem<Task>) myTree.getSelectionModel().getSelectedItem();
    }

    public void addItemAction(CheckBoxTreeItem<Task> taskItem, TaskUi taskUi) {
        if (taskItem == null || myTree.getRoot().getChildren().isEmpty()) {
            addButton.fire();
        } else {
            CheckBoxTreeItem<Task> it = taskItem;
            Task task = it.getValue();
            if (task instanceof Ziel && ((Ziel) task).getSize() > 0) {
                Task newTask = new Task(TASK_DEFAULT_NAME);
                taskUi.addZiele(newTask, it);
            } else {
                Ziel ziel;
                if (task instanceof Ziel) {
                    ziel = (Ziel) task;
                } else {
                    ziel = new Ziel(task);
                }

                it.setValue(ziel);
                Task newTask = new Task(TASK_DEFAULT_NAME);
                taskUi.addZiele(newTask, (CheckBoxTreeItem<Task>) it);
            }
        }
    }

    public void removeItemAction(CheckBoxTreeItem<Task> taskItem, TaskUi taskUi) {
        if (taskItem == null) {
            myTree.getSelectionModel().selectLast();
        } else {
            CheckBoxTreeItem<Task> it = taskItem;
            Task task = it.getValue();
            if (task.getClass() == Ziel.class) {
                ((Ziel) task).removeAllTasks();
            }
            taskUi.removeTask(task, taskItem);
        }
    }

    private void removeTask(Task t, CheckBoxTreeItem<Task> taskItem) {
        allTasks.remove(t);
        //root.getChildren().clear(); // Remove everything
        CheckBoxTreeItem<Task> parent = (CheckBoxTreeItem<Task>) taskItem.getParent();
        if (taskItem.getParent() != null) {
            parent.getChildren().remove(taskItem);
            try {
                ((Ziel) parent.getValue()).removeTask(t);
            } catch (Exception ignored) {

            }
        }
    }

    public void addTreeTask(Task task) {
        addZiele(task, (CheckBoxTreeItem<Task>) myTree.getRoot());
    }

    public void addZiele(Task task, CheckBoxTreeItem<Task> ast) {
        // HInzufügen bezüglich der Klasse
        if (task.getClass() == Ziel.class && ((Ziel) task).getSize() > 0) {
            Ziel z = (Ziel) task;
            CheckBoxTreeItem<Task> treeItem = new CheckBoxTreeItem<Task>(z);
            List<Task> tasks = z.getTasks().getTodos();
            tasks.forEach(t ->
            {
                if (t.getClass() == Ziel.class) {
                    addZiele((Ziel) t, treeItem);
                } else {
                    CheckBoxTreeItem<Task> taskItem = new CheckBoxTreeItem<Task>(t);
                    treeItem.getChildren().add(taskItem);
                    addListenerToCheckBoxTreeItem(taskItem);
                    signalChanges(treeItem);
                    changeStateCheckedBoxItem(taskItem, t);
                }
            });
            ast.getChildren().add(treeItem);
            addListenerToCheckBoxTreeItem(treeItem);
            signalChanges(treeItem);
            changeStateCheckedBoxItem(treeItem, task);

        } else {
            CheckBoxTreeItem<Task> treeItem = new CheckBoxTreeItem<Task>(task);
            ast.getChildren().add(treeItem);
            addListenerToCheckBoxTreeItem(treeItem);
            signalChanges(treeItem);
            changeStateCheckedBoxItem(treeItem, task);
        }
        allTasks.add(task);

    }

    private void changeStateCheckedBoxItem(CheckBoxTreeItem<Task> item, Task t) {
        if (t.isIndeterminate()) {
            item.setIndeterminate(true);
        } else {
            item.setSelected(t.isDone());
        }
    }

    private void addListenerToCheckBoxTreeItem(CheckBoxTreeItem<Task> item) {
        //item.indeterminateProperty().addListener((prop, old, newV) -> fireEvent(new StateChangedEvent(StateChangedEvent.CHANGED_EVENT_TYPE)));
        item.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                CheckBoxTreeItem<Task> test = getSelectedItem();
                fireEvent(new StateChangedEvent(StateChangedEvent.CHANGED_EVENT_TYPE));
                if (test != null) {
                    getTaskInfoUI().updateProgressBar(test.getValue(), test);

                }
            }
        });
    }

    public TaskInfoUI getTaskInfoUI() {
        return taskInfoUI;
    }

    public TreeView<Task> getMyTree() {
        return myTree;
    }


    public TreeItem<Task> getTreeRoot() {
        return myTree.getRoot();
    }

    public void setTaskInfoStyle(String style) {
        taskInfoUI.setPfadCss(style);
    }


    private void signalChanges(TreeItem<Task> item)
    {
        fireEvent(new StateChangedEvent(StateChangedEvent.CHANGED_EVENT_TYPE));
        item.getChildren().addListener(new ListChangeListener<TreeItem<Task>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<Task>> changed) {
                while (changed.next()) {
                    if ((changed.wasAdded() || changed.wasUpdated() || changed.wasRemoved())) {
                        fireEvent(new StateChangedEvent(StateChangedEvent.CHANGED_EVENT_TYPE));
                    }
                }
            }
        });
    }

    private void rootListener()
    {
        myTree.getRoot().getChildren().addListener(new ListChangeListener<TreeItem<Task>>() {

            @Override
            public void onChanged(Change<? extends TreeItem<Task>> changed) {
                while (changed.next()) {
                    if ((changed.wasAdded() || changed.wasUpdated() || changed.wasRemoved())) {
                        fireEvent(new StateChangedEvent(StateChangedEvent.CHANGED_EVENT_TYPE));
                    }
                }
            }
        });
    }
    private void signalChanges() {
        // fire an event if a change occurs in the list
        myTree.addEventHandler(StateChangedEvent.CHANGED_EVENT_TYPE, e ->
                fireEvent(new StateChangedEvent(StateChangedEvent.CHANGED_EVENT_TYPE)));
        rootListener();
        myTree.rootProperty().addListener((prop, old, newValue) ->
        {
           rootListener();
        });

    }
}
