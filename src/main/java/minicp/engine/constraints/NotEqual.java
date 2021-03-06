/*
 * mini-cp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License  v3
 * as published by the Free Software Foundation.
 *
 * mini-cp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 * See the GNU Lesser General Public License  for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mini-cp. If not, see http://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * Copyright (c)  2017. by Laurent Michel, Pierre Schaus, Pascal Van Hentenryck
 */

package minicp.engine.constraints;

import minicp.engine.core.Constraint;
import minicp.engine.core.IntVar;
import minicp.util.InconsistencyException;

public class NotEqual extends Constraint {

    private IntVar x, y;
    private int c;

    public NotEqual(IntVar x, IntVar y) { // x != y
        super(x.getSolver());
        this.x = x;
        this.y = y;
        this.c = 0;
    }
    public NotEqual(IntVar x, IntVar y, int c) { // x != y + c
        super(x.getSolver());
        this.x = x;
        this.y = y;
        this.c = c;
    }

    @Override
    public void post() throws InconsistencyException {
        if (y.isBound())
            x.remove(y.getMin() + c);
        else if (x.isBound())
            y.remove(x.getMin() - c);
        else {
            x.propagateOnBind(this);
            y.propagateOnBind(this);

            //x.whenBind(() -> y.remove(x.getMin() - c));
            //y.whenBind(() -> x.remove(y.getMin() + c));
        }
    }

    @Override
    public void propagate() throws InconsistencyException {
        if (y.isBound()) x.remove(y.getMin() + c);
        else y.remove(x.getMin() - c);
        this.deactivate();
    }

}
