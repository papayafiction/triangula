package de.sopamo.triangula.android.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class Hooks {
    public static final int PLAYER_JUMP = 0;
    public static final int TAP = 1;
    public static final int REWINDING = 2;
    public static final int STOP_REWINDING = 3;
    public static final int EXIT = 4;
    public static final int EXPLODE = 5;
    public static final int SWITCH = 6;
    public static final int BOUNCE = 7;
    public static final int MUTE = 8;
    public static final int UNMUTE = 9;
    public static final int STOP_ALL = 10;
    public static final int DESTROY_ALL = 11;
    public static final int RESUME = 12;
    public static final int MENU_START = 13;
    public static final int MENU_STOP = 14;


    private static HashMap<Integer, ArrayList<Callable<Integer>>> hooks = new HashMap<Integer, ArrayList<Callable<Integer>>>();

    public static void bind(int key, Callable<Integer> callback)
    {
        if(!hooks.containsKey(key))
        {
            hooks.put(key,new ArrayList<Callable<Integer>>());
        }
        hooks.get(key).add(callback);
    }

    public static void clear()
    {
        hooks = new HashMap<Integer, ArrayList<Callable<Integer>>>();
    }

    public static void remove(int key) {
        if(hooks.containsKey(key)) {
            hooks.remove(key);
        }
    }

    public static void call(int key)
    {
        if(!hooks.containsKey(key))
        {
            return;
        }
        List<Callable<Integer>> list = hooks.get(key);
        if(list != null)
        {
            for(int i = 0; i < list.size(); ++i)
            {
                Callable<Integer> callback = list.get(i);
                try
                {
                    callback.call();
                } catch (Exception e)
                {

                }
            }
        }
    }
}