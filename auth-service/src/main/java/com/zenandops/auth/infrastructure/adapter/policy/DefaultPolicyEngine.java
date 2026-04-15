package com.zenandops.auth.infrastructure.adapter.policy;

import com.zenandops.auth.application.port.PolicyEngine;
import com.zenandops.auth.domain.entity.User;
import com.zenandops.auth.domain.valueobject.AbacPolicy;
import com.zenandops.auth.domain.valueobject.RbacPolicy;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

/**
 * Default adapter implementing the PolicyEngine port.
 * Evaluates RBAC rules by role matching and ABAC rules by attribute matching.
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
        Map<String, String> userAttributes = user.getAttributes();
        if (userAttributes == null || userAttributes.isEmpty()) {
            return false;
        }
        // User attributes must match ALL required user attributes in the policy
        return policy.requiredUserAttributes().entrySet().stream()
                .allMatch(entry -> entry.getValue().equals(userAttributes.get(entry.getKey())));
    }
}
