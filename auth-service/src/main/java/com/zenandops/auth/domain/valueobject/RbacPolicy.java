package com.zenandops.auth.domain.valueobject;

import java.util.Set;

/**
 * Value object representing an RBAC authorization rule.
 * Access is granted when the user holds at least one of the required roles
 * for the specified resource and action.
 *
 * @param requiredRoles the set of roles that grant access
 * @param resource      the protected resource identifier
 * @param action        the action being performed on the resource
 */
public record RbacPolicy(
        Set<String> requiredRoles,
        String resource,
        String action
) {
}
