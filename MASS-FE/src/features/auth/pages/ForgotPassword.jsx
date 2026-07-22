import { Form, Button, Card, Container } from "react-bootstrap";
import authService from "../services/auth.service";
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const ForgotPassword = () => {
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await authService.forgotPassword(email);
      setMessage("OTP sent to your email!");
      setTimeout(() => navigate(`/verify-otp?email=${email}`), 2000);
    } catch (error) {
      if (error.response && error.response.data && error.response.data.message) {
        setError(error.response.data.message);
      } else {
        setError("User not found or error occurred.");
      }
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: "100vh" }}>
      <Card className="p-4 shadow-sm border rounded-4" style={{ width: "400px" }}>
        <h2 className="text-center mb-4">Forgot Password</h2>
        <p className="text-muted text-center">Enter your email to receive a password reset OTP.</p>
        {error && <div className="alert alert-danger p-2 text-center">{error}</div>}
        {message && <div className="alert alert-success p-2 text-center">{message}</div>}
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Email address</Form.Label>
            <Form.Control type="email" placeholder="Enter email" required onChange={(e) => setEmail(e.target.value)} />
          </Form.Group>
          <Button variant="warning" type="submit" className="w-100 mb-3 text-white fw-bold">Send OTP</Button>
          <div className="text-center">
            <Link to="/login">Back to Login</Link>
          </div>
        </Form>
      </Card>
    </Container>
  );
};
export default ForgotPassword;
