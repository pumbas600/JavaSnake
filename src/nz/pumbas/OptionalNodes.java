package nz.pumbas;

import javafx.scene.Node;

import nz.pumbas.UtilityClasses.Condition;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OptionalNodes
{
    private ArrayList<Node> optionalNodes = new ArrayList<>();
    private Condition condition;

    public OptionalNodes(Condition condition) {
        this.condition = condition;
    }

    public void addNode(Node node) {
        optionalNodes.add(node);
    }

    public void addNodes(Node[] nodes) {
        optionalNodes.addAll(Stream.of(nodes)
                .collect(Collectors.toList()));
    }

    public ArrayList<Node> getOptionalNodes() {
        return optionalNodes;
    }

    public boolean evaluateCondition() {
        return condition.condition();
    }
}
