package org.kebab.api.events.permission;

import org.kebab.api.events.Event;
import org.kebab.api.permission.PermissionWrapper;

/**
 * Called when the server starts and a permission system gets chosen.
 */
public final class PermissionSetupEvent extends Event {
    private PermissionWrapper permissionWrapper;

    public PermissionSetupEvent(PermissionWrapper permissionWrapper) {
        this.permissionWrapper = permissionWrapper;
    }

    public PermissionWrapper getPermissionWrapper() {
        return permissionWrapper;
    }

    public void setPermissionWrapper(PermissionWrapper permissionWrapper) {
        if (permissionWrapper == null) throw new NullPointerException("PermissionWrapper cannot be null");
        this.permissionWrapper = permissionWrapper;
    }
}
