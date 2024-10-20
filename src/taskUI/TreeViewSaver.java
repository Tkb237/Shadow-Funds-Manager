package taskUI;

import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import taskVerwaltung.Task;
import taskVerwaltung.Ziel;

// save all Tasks of a TreeView
public class TreeViewSaver {
    private static Task go(CheckBoxTreeItem<Task> treeItem) {

        Ziel myZiel = new Ziel(treeItem.getValue());
        myZiel.setDone(treeItem.isSelected());
        myZiel.setIndeterminate(treeItem.isIndeterminate());

        treeItem.getChildren().forEach(e ->
                {
                    if (!e.isLeaf()) {
                        myZiel.addTask(go((CheckBoxTreeItem<Task>) e));
                    } else {
                        Task task = e.getValue();
                        task.setDone(((CheckBoxTreeItem<Task>) e).isSelected());
                        task.setIndeterminate(((CheckBoxTreeItem<Task>) e).isIndeterminate());
                        myZiel.addTask(task);
                    }
                }
        );


        return myZiel;
    }

    public static Task save(TreeView<Task> t) {
        return go((CheckBoxTreeItem<Task>) t.getRoot());
    }

}
