import { Card, Form, Button } from "react-bootstrap";
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const ForgotPassword = () => {
    const [email, setEmail] = useState("");
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();

        alert("OTP has been sent to your email.");

        navigate("/verify-otp");
    };

    return (
        <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
            <Card
                className="p-4 shadow-sm border rounded-4"
                style={{ width: "500px" }}
            >
                <h2 className="text-center fw-bold">
                    Forgot Password
                </h2>

                <p className="text-center text-muted mb-4">
                    Enter your email address to receive an OTP code
                </p>

                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Control
                            type="email"
                            placeholder="Email Address"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </Form.Group>

                    <Button
                        type="submit"
                        variant="primary"
                        className="w-100"
                    >
                        Send OTP
                    </Button>

                    <div className="text-center mt-3">
                        <Link to="/login">
                            Back to Login
                        </Link>
                    </div>
                </Form>
            </Card>
        </div>
    );
};

export default ForgotPassword;