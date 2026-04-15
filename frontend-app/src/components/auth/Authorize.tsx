import { useIsAuthorized } from "../../hooks/useAuthorization";

interface AuthorizeProps {
  /** User must have at least one of these roles */
  roles?: string[];
  /** User must match all of these attribute key-value pairs */
  attributes?: Record<string, string>;
  /** Content to render when authorized */
  children: React.ReactNode;
  /** Optional fallback when not authorized (defaults to nothing) */
  fallback?: React.ReactNode;
}

/**
 * Conditionally renders children based on JWT claims (roles and attributes).
 * Hides content by default when the user lacks authorization.
 */
export default function Authorize({
  roles,
  attributes,
  children,
  fallback = null,
}: AuthorizeProps) {
  const authorized = useIsAuthorized({ roles, attributes });

  if (!authorized) {
    return <>{fallback}</>;
  }

  return <>{children}</>;
}
