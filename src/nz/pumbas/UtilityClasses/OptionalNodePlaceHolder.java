package nz.pumbas.UtilityClasses;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.scene.Node;

public class OptionalNodePlaceHolder extends Node
{
    public int index;
    public OptionalNodePlaceHolder(int index) {
        this.index = index;
    }

    @Override
    protected NGNode impl_createPeer()
    {
        return null;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx)
    {
        return null;
    }

    @Override
    protected boolean impl_computeContains(double localX, double localY)
    {
        return false;
    }

    @Override
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx)
    {
        return null;
    }
}
