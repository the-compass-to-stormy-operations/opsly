package com.zenandops.auth.infrastructure.adapter.policy;

import com.zenandops.auth.application.port.PolicyEngine;
import com.zenandops.auth.domain.entity.User;
import com.zenandops.auth.domain.valueobject.AbacPolicy;
import com.zenandops.auth.domain.valueobject.RbacPolicy;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Default adapter implementing the PolicyEngine port.
 * Evaluates RBAC rules by role matching and ABAC rules by Tag key-value matching.
 */
@ApplicationScoped
public class DefaultPolicyEngine implements PolicyEngine {

    @Override
    public boolean evaluateRbac(User user, RbacPolicy policy) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return false;
        }
        if (policy.requiredRoles() == null || policy.requiredRoles().isEmpty()) {
            return true;
        }
        // User must have at least one of the required roles
        return user.getRoles().stream()
                .anyMatch(policy.requiredRoles()::contains);
    }

    @Override
    public boolean evaluateAbac(User user, AbacPolicy policy) {
        if (policy.requiredUserAttributes() == null || policy.requiredUserAttributes().isEmpty()) {
            return true;
        }
        // User must have tags assigned to be evaluated against ABAC policies.
        // Full Tag key-value resolution is performed in the infrastructure layer (Task 3.4).
        if (user.getTagIds() == null || user.getTagIds().isEmpty()) {
            return false;
        }
        // Placeholder: full Tag resolution and matching will be implemented in Task 3.4
        return false;
    }
}
