import { Form, Button, Card } from "react-bootstrap";
import authService from "../services/authService";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";

const Register = () => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = { name, email, password };

        try {
            const response = await authService.register(payload);

            console.log("Register response:", response);

            alert("Register successful! Please login.");

            // sau khi đăng ký xong → chuyển sang login
            navigate("/login");

        } catch (error) {
            console.error(error);
            alert("Register failed!");
        }
    };

    return (
        <Card className="p-4 shadow-sm border rounded-4" style={{ width: "500px" }}>
            <h1 className="text-center">Create Account</h1>
            <h4 className="text-center text-muted fs-6 pb-3">
                Sign up to start using the system
            </h4>

            <Form onSubmit={handleSubmit}>
                {/* Name */}
                <Form.Group className="mb-3">
                    <Form.Control
                        type="text"
                        placeholder="Full Name"
                        onChange={(e) => setName(e.target.value)}
                    />
                </Form.Group>

                {/* Email */}
                <Form.Group className="mb-3">
                    <Form.Control
                        type="email"
                        placeholder="Email Address"
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </Form.Group>

                {/* Password */}
                <Form.Group className="mb-3">
                    <Form.Control
                        type="password"
                        placeholder="Password"
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </Form.Group>

                <Button
                    type="submit"
                    variant="primary"
                    className="w-100 d-flex align-items-center justify-content-center"
                >
                    Sign Up
                </Button>

                <div className="text-center mt-3">
                    Already have an account?{" "}
                    <Link to="/login">Login</Link>
                </div>
            </Form>
        </Card>
    );
};

export default Register;