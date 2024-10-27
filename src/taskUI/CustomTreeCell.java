package taskUI;

import event.StateChangedEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.input.KeyCode;
import taskVerwaltung.Task;
import taskVerwaltung.Ziel;

// it used to update the tree view item and monitor changes
class CustomTreeCell extends CheckBoxTreeCell<Task> {
    private TextField myText = new TextField();
    private final ContextMenu myContext = new ContextMenu();
    private final TaskUi taskUi;

    public CustomTreeCell(TaskUi taskUi) {
        this.taskUi = taskUi;
        configMenuItem(this.taskUi);
        // send data to the "TaskInfoUI" if we click on a TreeItem
        setOnMouseClicked(event  ->
        {
            CheckBoxTreeItem<Task> item = (CheckBoxTreeItem<Task>) getTreeItem();
            if(item != null) taskUi.getTaskInfoUI().setData(item.getValue(), item);
        });
    }

    public static double getAnzahlCheckedTasks(CheckBoxTreeItem<Task> it) {
        double counter = 0;

        Task task = it.getValue();

        if (task instanceof Ziel) {
            @SuppressWarnings("unchecked")
            CheckBoxTreeItem<Task>[] tasksItems = it.getChildren().stream().map(s -> (CheckBoxTreeItem<Task>) s).toArray(CheckBoxTreeItem[]::new);
            for (CheckBoxTreeItem<Task> s : tasksItems) {
                counter += CustomTreeCell.getAnzahlCheckedTasks(s);
            }
            return counter;
        } else if (it.isSelected()) return 1;
        else return counter;

    }

    private void configMenuItem(TaskUi taskUi) {
        MenuItem removeMenuItem = new MenuItem("Entfernen");
        MenuItem addMenuItem = new MenuItem("Hinzufügen");
        myContext.getItems().add(addMenuItem);
        myContext.getItems().add(removeMenuItem);
        setContextMenu(myContext);
        addMenuItem.setOnAction(s ->
        {
            CheckBoxTreeItem<Task> it = (CheckBoxTreeItem<Task>) getTreeItem();
            taskUi.addItemAction(it, taskUi);
        });

        removeMenuItem.setOnAction(s ->
        {
            CheckBoxTreeItem<Task> it = (CheckBoxTreeItem<Task>) getTreeItem();
            taskUi.removeItemAction(it, taskUi);
        });

    }

    @Override
    public void startEdit() {
        createTextField();
        super.startEdit();
        setText(null);
        setGraphic(myText);
        myText.requestFocus();
        myText.selectAll();
    }

    @Override
    public void updateItem(Task task, boolean leer) {
        super.updateItem(task, leer);
        // change text color based to priority
        TaskInfoUI.setPriorityColor(getTreeItem(), this);
        if (leer) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (myText != null) {
                    myText.setText(getString());
                }
                setText(null);
                setGraphic(myText);
            } else {
                setText(getString());
            }
        }
    }

    @Override
    public void cancelEdit() {
        try {
            super.cancelEdit();
            setText(getItem().toString());
            setGraphic(getTreeItem().getGraphic());
        } catch (Exception e) {
            super.cancelEdit();
            setText(getItem().toString());
            setGraphic(getTreeItem().getGraphic());
        }

    }

    private void createTextField() {
        myText = new TextField(getString());
        myText.setOnKeyReleased(e ->
        {
            if (e.getCode() == KeyCode.ENTER) {
                if (myText.getText().isBlank() || myText.getText().isEmpty()) cancelEdit();
                else {
                    getItem().setBezeichnung(myText.getText());
                    commitEdit(getItem());
                    // emit a signal if a task was renamed
                    getTreeView().fireEvent(new StateChangedEvent(StateChangedEvent.CHANGED_EVENT_TYPE));
                    taskUi.getTaskInfoUI().updateBezeichnung(myText.getText());

                }
            } else if (e.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                getTreeView().refresh();
            }

        });

        myText.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                if (!arg2) {
                    cancelEdit();
                    getTreeView().refresh();
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "Null" : getItem().toString();
    }

}