import { Form, Button, Card, Container } from "react-bootstrap";
import authService from "../services/auth.service";
import { useContext, useState } from "react";
import { AuthContext } from "../../../app/providers/AuthProvider";
import { useNavigate, Link } from "react-router-dom";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { login } = useContext(AuthContext);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await authService.login({ email, password });
      login(response.data);
      if(response.data.role === "ROLE_ADMIN") {
        navigate("/dashboard");
      } else {
        navigate("/profile");
      }
    } catch (error) {
      setError("Invalid email or password.");
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: "100vh" }}>
      <Card className="p-4 shadow-sm border rounded-4" style={{ width: "400px" }}>
        <h2 className="text-center mb-4">Login to MASS</h2>
        {error && <div className="alert alert-danger p-2 text-center">{error}</div>}
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Email address</Form.Label>
            <Form.Control type="email" placeholder="Enter email" required onChange={(e) => setEmail(e.target.value)} />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Password</Form.Label>
            <Form.Control type="password" placeholder="Password" required onChange={(e) => setPassword(e.target.value)} />
          </Form.Group>
          <Button variant="primary" type="submit" className="w-100 mb-3">Sign In</Button>
          <div className="d-flex justify-content-between">
            <Link to="/forgot-password">Forgot password?</Link>
            <Link to="/register">Create an account</Link>
          </div>
        </Form>
      </Card>
    </Container>
  );
};
export default Login;
