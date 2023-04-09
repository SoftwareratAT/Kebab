package org.kebab.server;

import org.jetbrains.annotations.Nullable;
import org.kebab.api.events.permission.PermissionSetupEvent;
import org.kebab.api.permission.PermissionWrapper;
import org.kebab.common.KebabRegistry;
import org.kebab.server.events.KebabEventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class KebabPermissions {
    private static final Logger LOGGER = LoggerFactory.getLogger(KebabPermissions.class);

    private PermissionWrapper currentDefaultPermissionWrapper;

    public KebabPermissions() {
        KebabRegistry.register(this);
        this.currentDefaultPermissionWrapper = new DefaultPermissionWrapper();
    }

    CompletableFuture<Void> callPermissionSetupEvent() {
        return CompletableFuture.runAsync(() -> {
            PermissionSetupEvent permissionSetupEvent = new PermissionSetupEvent(this.currentDefaultPermissionWrapper);
            Optional<KebabEventManager> optionalKebabEventManager = KebabRegistry.get(KebabEventManager.class);
            if (optionalKebabEventManager.isEmpty() || true) return; // Returns cause Events not setup yet
            KebabEventManager eventManager = optionalKebabEventManager.get();
            try {
                this.currentDefaultPermissionWrapper = eventManager.callEvent(permissionSetupEvent).get().getPermissionWrapper();
            } catch (Exception exception) {
                LOGGER.error("Cannot process PermissionSetupEvent", exception);
            }
        });
    }

    public PermissionWrapper getCurrentDefaultPermissionWrapper() {
        return currentDefaultPermissionWrapper;
    }

    private static final class DefaultPermissionWrapper implements PermissionWrapper {
        @Override
        public boolean hasPermission(@Nullable String node) {
            return false;
        }
    }
}
