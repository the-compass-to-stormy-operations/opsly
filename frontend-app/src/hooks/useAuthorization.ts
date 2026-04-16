import { useAuth } from "../context/AuthContext";

/**
 * Check if the user has at least one of the required roles.
 */
export function useHasRole(...requiredRoles: string[]): boolean {
  const { user } = useAuth();
  if (!user || !user.roles) return false;
  return requiredRoles.some((role) => user.roles.includes(role));
}

/**
 * Check if the user has all of the required attributes (key-value match).
 */
export function useHasAttributes(
  requiredAttributes: Record<string, string>
): boolean {
  const { user } = useAuth();
  if (!user || !user.attributes) return false;
  return Object.entries(requiredAttributes).every(
    ([key, value]) => user.attributes[key] === value
  );
}

/**
 * Combined RBAC + ABAC check.
 * Pass roles, attributes, or both. All provided conditions must be satisfied.
 */
export function useIsAuthorized(options: {
  roles?: string[];
  attributes?: Record<string, string>;
}): boolean {
  const { user } = useAuth();
  if (!user) return false;

  if (options.roles && options.roles.length > 0) {
    const hasRole = options.roles.some((role) => user.roles?.includes(role));
    if (!hasRole) return false;
  }

  if (options.attributes) {
    const hasAttrs = Object.entries(options.attributes).every(
      ([key, value]) => user.attributes?.[key] === value
    );
    if (!hasAttrs) return false;
  }

  return true;
}
