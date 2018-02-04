package com.example.basiclauncher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 3/02/18.
 */

public class ShortcutSerializableData implements Serializable {
    private static final long serialVersionUID = 2292680772212306903L;

    List<ShortcutPac> apps = new ArrayList<ShortcutPac>();

    public ShortcutPac findPac(String UUIDIdentifyer){
        for (ShortcutPac p: apps){
            if (p.UUIDIdentifyer.equals(UUIDIdentifyer))
                return p;
        }

        return null;
    }
}
