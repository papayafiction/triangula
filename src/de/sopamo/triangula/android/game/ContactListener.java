package de.sopamo.triangula.android.game;

import android.util.Log;
import de.sopamo.box2dbridge.IShape;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

public class ContactListener implements org.jbox2d.dynamics.ContactListener {
    @Override
    public void add(ContactPoint point) {
        IShape userData1 = (IShape)point.shape1.getUserData();
        IShape userData2 = (IShape)point.shape2.getUserData();
        if(userData1 != null && userData1.getUserData() instanceof String) {
            Log.e("foouserdata1", (String) userData1.getUserData());
        }
        if(userData2 != null && userData2.getUserData() instanceof String) {
            Log.e("foouserdata2", (String) userData2.getUserData());
        }
    }

    @Override
    public void persist(ContactPoint point) {

    }

    @Override
    public void remove(ContactPoint point) {

    }

    @Override
    public void result(ContactResult point) {

    }
}
