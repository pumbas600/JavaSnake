package nz.pumbas.UtilityClasses;

import com.sun.istack.internal.NotNull;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import nz.pumbas.OptionalNodes;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PopupBuilder<T extends Pane>
{
    private Popup popup = new Popup();
    private T root;

    private boolean addOptional = false;
    private ArrayList<OptionalNodes> optionalNodes = new ArrayList<>();
    private ArrayList<Node> nodesArray = new ArrayList<>();
    private Node latestNode;

    public PopupBuilder(T root) {
        this.root = root;
        popup.setAutoHide(true); //By default this will be enabled
        latestNode = root;
    }

    @NotNull
    public PopupBuilder addNode(Node node) {
        if (addOptional) {
            //Add the node to the latest optional set
            optionalNodes.get(optionalNodes.size() - 1).addNode(node);
        }
        else nodesArray.add(node);
        latestNode = node;
        return this;
    }

    @NotNull
    public PopupBuilder addNodes(Node... nodes) {
        if (addOptional) {
            optionalNodes.get(optionalNodes.size() - 1).addNodes(nodes);
        }
        else nodesArray.addAll(Stream.of(nodes)
                .collect(Collectors.toList()));
        latestNode = nodes[nodes.length - 1];
        return this;
    }

    public PopupBuilder setPadding(int padding) {
        root.setPadding(new Insets(padding));
        return this;
    }

    public PopupBuilder setBorder(Border border) {
        root.setBorder(border);
        return this;
    }

    public PopupBuilder setStyle(String style) {
        latestNode.setStyle(style);
        return this;
    }

    public PopupBuilder setId(String id) {
        latestNode.setId(id);
        return this;
    }

    public PopupBuilder setOnMouseClicked(EventHandler<? super MouseEvent> event) {
        latestNode.setOnMouseClicked(event);
        return this;
    }

    public PopupBuilder setOnKeyPressed(EventHandler<? super KeyEvent> event) {
        latestNode.setOnKeyPressed(event);
        return this;
    }

    public PopupBuilder setPopupEvent(EventType<Event> event, EventHandler<? super Event> eventHandler) {
        popup.addEventHandler(event, eventHandler);
        return this;
    }

    public PopupBuilder setPopupSize(double width, double height) {
        popup.setWidth(width);
        popup.setHeight(height);
        return this;
    }

    public PopupBuilder disablePopupAutoHide() {
        popup.setAutoHide(false);
        return this;
    }

    public PopupBuilder addCloseButton() {
        Button close = new Button("X");
        close.setStyle("-fx-background-color: red; -fx-text-fill: white");
        close.setOnMouseClicked(event -> {
            popup.hide();
        });
        close.setAlignment(Pos.TOP_LEFT);
        nodesArray.add(close);
        return this;
    }


    public PopupBuilder startOptionalNodes(Condition condition) {
        addOptional = true;
        optionalNodes.add(new OptionalNodes(condition));
        nodesArray.add(new OptionalNodePlaceHolder(optionalNodes.size() - 1));
        return this;
    }

    public PopupBuilder endOptionalNodes() {
        addOptional = false;
        return this;
    }

    public Popup build() {
        setupBuild();
        return popup;
    }

    public Popup build(Stage primaryStage) {
        setupBuild();
        popup.show(primaryStage);
        return popup;
    }

    private void setupBuild() {
        if (optionalNodes.size() != 0)
        {
            setConditionEvents();
        }
        else {
            //Add all the nodes to the popup - As optionalNodes.size == 0, there is therefore no
            //placeholders in the arraylist.
            root.getChildren().addAll(nodesArray);
        }
        popup.getContent().add(root);
    }

    private void setConditionEvents() {
        popup.setOnShowing(event -> {

            //Add all nodes and optional nodes to the toDisplay arraylist if their condition is true:
            ArrayList<Node> toDisplay = new ArrayList<>();
            for (Node node : nodesArray) {
                if (node instanceof OptionalNodePlaceHolder) {
                    OptionalNodePlaceHolder placeHolder =  (OptionalNodePlaceHolder) node;
                    OptionalNodes optionalNode = optionalNodes.get(placeHolder.index);
                    if (optionalNode.evaluateCondition())
                        toDisplay.addAll(optionalNode.getOptionalNodes());
                }
                else {
                    toDisplay.add(node);
                }
            }
            //First remove all currentNodes
            root.getChildren().clear();
            root.getChildren().addAll(toDisplay);
        });
    }





}
