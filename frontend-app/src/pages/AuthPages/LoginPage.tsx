import { useState, FormEvent } from "react";
import { useNavigate } from "react-router";
import { EyeCloseIcon, EyeIcon } from "../../icons";
import Label from "../../components/form/Label";
import Input from "../../components/form/input/InputField";
import Button from "../../components/ui/button/Button";
import PageMeta from "../../components/common/PageMeta";
import AuthLayout from "./AuthPageLayout";
import { useAuth } from "../../context/AuthContext";

export default function LoginPage() {
  const [loginId, setLoginId] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<{
    login?: string;
    password?: string;
  }>({});

  const { login } = useAuth();
  const navigate = useNavigate();

  function validate(): boolean {
    const errors: { login?: string; password?: string } = {};

    if (!loginId.trim()) {
      errors.login = "Login is required";
    }
    if (!password) {
      errors.password = "Password is required";
    }

    setFieldErrors(errors);
    return Object.keys(errors).length === 0;
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);

    if (!validate()) {
      return;
    }

    setIsLoading(true);
    try {
      await login(loginId, password);
      navigate("/", { replace: true });
    } catch {
      // Generic error message — do not reveal which field is wrong
      setError("Authentication failed. Please check your credentials.");
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <>
      <PageMeta
        title="Login | ZenAndOps"
        description="ZenAndOps login page"
      />
      <AuthLayout>
        <div className="flex flex-col flex-1">
          <div className="flex flex-col justify-center flex-1 w-full max-w-md mx-auto">
            <div>
              <div className="mb-5 sm:mb-8">
                <h1 className="mb-2 font-semibold text-gray-800 text-title-sm dark:text-white/90 sm:text-title-md">
                  Sign In
                </h1>
                <p className="text-sm text-gray-500 dark:text-gray-400">
                  Enter your login and password to access ZenAndOps.
                </p>
              </div>
              <div>
                {error && (
                  <div
                    className="mb-4 rounded-lg border border-error-300 bg-error-50 p-3 text-sm text-error-600 dark:border-error-500/30 dark:bg-error-500/10 dark:text-error-400"
                    role="alert"
                  >
                    {error}
                  </div>
                )}
                <form onSubmit={handleSubmit} noValidate>
                  <div className="space-y-6">
                    <div>
                      <Label htmlFor="login">
                        Login <span className="text-error-500">*</span>
                      </Label>
                      <Input
                        id="login"
                        name="login"
                        type="text"
                        placeholder="Enter your login"
                        value={loginId}
                        onChange={(e) => {
                          setLoginId(e.target.value);
                          if (fieldErrors.login) {
                            setFieldErrors((prev) => ({ ...prev, login: undefined }));
                          }
                        }}
                        error={!!fieldErrors.login}
                        hint={fieldErrors.login}
                        disabled={isLoading}
                      />
                    </div>
                    <div>
                      <Label htmlFor="password">
                        Password <span className="text-error-500">*</span>
                      </Label>
                      <div className="relative">
                        <Input
                          id="password"
                          name="password"
                          type={showPassword ? "text" : "password"}
                          placeholder="Enter your password"
                          value={password}
                          onChange={(e) => {
                            setPassword(e.target.value);
                            if (fieldErrors.password) {
                              setFieldErrors((prev) => ({
                                ...prev,
                                password: undefined,
                              }));
                            }
                          }}
                          error={!!fieldErrors.password}
                          hint={fieldErrors.password}
                          disabled={isLoading}
                        />
                        <button
                          type="button"
                          onClick={() => setShowPassword(!showPassword)}
                          className="absolute z-30 -translate-y-1/2 cursor-pointer right-4 top-1/2"
                          aria-label={showPassword ? "Hide password" : "Show password"}
                        >
                          {showPassword ? (
                            <EyeIcon className="fill-gray-500 dark:fill-gray-400 size-5" />
                          ) : (
                            <EyeCloseIcon className="fill-gray-500 dark:fill-gray-400 size-5" />
                          )}
                        </button>
                      </div>
                    </div>
                    <div>
                      <Button className="w-full" size="sm" disabled={isLoading}>
                        {isLoading ? (
                          <span className="flex items-center gap-2">
                            <svg
                              className="animate-spin h-4 w-4"
                              xmlns="http://www.w3.org/2000/svg"
                              fill="none"
                              viewBox="0 0 24 24"
                            >
                              <circle
                                className="opacity-25"
                                cx="12"
                                cy="12"
                                r="10"
                                stroke="currentColor"
                                strokeWidth="4"
                              />
                              <path
                                className="opacity-75"
                                fill="currentColor"
                                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"
                              />
                            </svg>
                            Signing in...
                          </span>
                        ) : (
                          "Sign in"
                        )}
                      </Button>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </AuthLayout>
    </>
  );
}
