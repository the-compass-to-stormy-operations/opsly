import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router";
import LoginPage from "./pages/AuthPages/LoginPage";
import NotFound from "./pages/OtherPage/NotFound";
import TagManagement from "./pages/TagManagement";
import AppLayout from "./layout/AppLayout";
import { ScrollToTop } from "./components/common/ScrollToTop";
import Home from "./pages/Dashboard/Home";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import Authorize from "./components/auth/Authorize";
import { useAuth } from "./context/AuthContext";

function LoginRoute() {
  const { isAuthenticated } = useAuth();
  if (isAuthenticated) {
    return <Navigate to="/" replace />;
  }
  return <LoginPage />;
}

export default function App() {
  return (
    <Router>
      <ScrollToTop />
      <Routes>
        {/* Protected Dashboard Layout */}
        <Route
          element={
            <ProtectedRoute>
              <AppLayout />
            </ProtectedRoute>
          }
        >
          <Route index path="/" element={<Home />} />
          <Route
            path="/tags"
            element={
              <Authorize roles={["ADMIN"]} fallback={<Navigate to="/" replace />}>
                <TagManagement />
              </Authorize>
            }
          />
        </Route>

        {/* Public: Login (redirects authenticated users to dashboard) */}
        <Route path="/login" element={<LoginRoute />} />

        {/* Fallback */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  );
}
