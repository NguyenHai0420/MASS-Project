import { Form, Button, Card, Container } from "react-bootstrap";
import authService from "../services/auth.service";
import { useState } from "react";
import { useNavigate, useSearchParams, Link } from "react-router-dom";

const VerifyOtp = () => {
  const [searchParams] = useSearchParams();
  const email = searchParams.get("email");
  const [otp, setOtp] = useState("");
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await authService.verifyOtp(email, otp);
      navigate(`/reset-password?email=${email}&otp=${otp}`);
    } catch (error) {
      setError("Invalid or expired OTP.");
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: "100vh" }}>
      <Card className="p-4 shadow-sm border rounded-4" style={{ width: "400px" }}>
        <h2 className="text-center mb-4">Verify OTP</h2>
        <p className="text-muted text-center">Enter the 6-digit OTP sent to <b>{email}</b></p>
        {error && <div className="alert alert-danger p-2 text-center">{error}</div>}
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Control type="text" placeholder="123456" maxLength={6} required className="text-center fs-4 tracking-widest" onChange={(e) => setOtp(e.target.value)} />
          </Form.Group>
          <Button variant="primary" type="submit" className="w-100 mb-3">Verify</Button>
          <div className="text-center">
             <Link to="/forgot-password">Resend OTP</Link>
          </div>
        </Form>
      </Card>
    </Container>
  );
};
export default VerifyOtp;
