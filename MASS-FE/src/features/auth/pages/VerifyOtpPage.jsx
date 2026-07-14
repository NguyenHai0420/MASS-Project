import { Card, Form, Button } from "react-bootstrap";
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const VerifyOtpPage = () => {
    const [otp, setOtp] = useState("");
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();

        if (otp === "123456") {
            alert("OTP verified successfully!");
            navigate("/reset-password");
        } else {
            alert("Invalid OTP!");
        }
    };

    return (
        <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
            <Card className="p-4 shadow-sm border rounded-4" style={{ width: "500px" }}>
                <h1 className="text-center">Verify OTP</h1>

                <p className="text-center text-muted mb-4">
                    Enter the OTP code sent to your email
                </p>

                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Control
                            type="text"
                            placeholder="Enter OTP Code"
                            value={otp}
                            onChange={(e) => setOtp(e.target.value)}
                            maxLength={6}
                            required
                        />
                    </Form.Group>

                    <Button type="submit" variant="primary" className="w-100">
                        Verify OTP
                    </Button>

                    <div className="text-center mt-3">
                        <Link to="/forgot-password">Back</Link>
                    </div>
                </Form>
            </Card>
        </div>
    );
};

export default VerifyOtpPage;