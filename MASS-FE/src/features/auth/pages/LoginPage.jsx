import { Form, Button, Card } from "react-bootstrap";
import authService from "../services/authService";
import { useContext, useState } from "react";
import { AuthContext } from "../../../app/providers/AuthProvider";
import { useNavigate, Link } from "react-router-dom";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const { login } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const payload = { email, password };

            const response = await authService.login(payload);

            console.log("Login response:", response);

            login(response);

            alert(`Welcome, ${response.name}!`);

            navigate("/home");
        } catch (error) {
            alert("Invalid email or password!");
            console.error(error);
        }
    };

    return (
        <div className="d-flex justify-content-center align-items-center vh-100 bg-light"> 
        <Card
            className="p-4 shadow-sm border rounded-4"
            style={{ width: "500px" }}
        >
            <h2 className="text-center fw-bold">
                Medical Appointment System
            </h2>

            <p className="text-center text-muted mb-4">
                Sign in to manage your appointments and medical records
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

                <Form.Group className="mb-2">
                    <Form.Control
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </Form.Group>

                <div className="text-end mb-3">
                    <Link
                        to="/forgot-password"
                        className="text-decoration-none"
                    >
                        Forgot Password?
                    </Link>
                </div>

                <Button
                    type="submit"
                    variant="success"
                    className="w-100"
                >
                    Sign In
                </Button>

                <div className="text-center mt-3">
                    Don't have an account?{" "}
                    <Link
                        to="/register"
                        className="text-decoration-none"
                    >
                        Register
                    </Link>
                </div>
            </Form>
        </Card>
        </div>
    );
};

export default Login;