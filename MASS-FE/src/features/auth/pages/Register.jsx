import { Form, Button, Card, Container } from "react-bootstrap";
import authService from "../services/auth.service";
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const Register = () => {
  const [formData, setFormData] = useState({ fullName: "", email: "", password: "" });
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await authService.register(formData);
      setSuccess(true);
      setTimeout(() => navigate(`/verify-otp?email=${formData.email}&type=register`), 2000);
    } catch (error) {
      setError("Registration failed. Email might already exist.");
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: "100vh" }}>
      <Card className="p-4 shadow-sm border rounded-4" style={{ width: "400px" }}>
        <h2 className="text-center mb-4">Register for MASS</h2>
        {error && <div className="alert alert-danger p-2 text-center">{error}</div>}
        {success && <div className="alert alert-success p-2 text-center">Registration successful! Redirecting to OTP verification...</div>}
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Full Name</Form.Label>
            <Form.Control type="text" placeholder="Enter full name" required onChange={(e) => setFormData({...formData, fullName: e.target.value})} />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Email address</Form.Label>
            <Form.Control type="email" placeholder="Enter email" required onChange={(e) => setFormData({...formData, email: e.target.value})} />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Password</Form.Label>
            <Form.Control type="password" placeholder="Password (min 6 chars)" minLength={6} required onChange={(e) => setFormData({...formData, password: e.target.value})} />
          </Form.Group>
          <Button variant="success" type="submit" className="w-100 mb-3">Register</Button>
          <div className="text-center">
            <Link to="/login">Already have an account? Login</Link>
          </div>
        </Form>
      </Card>
    </Container>
  );
};
export default Register;
