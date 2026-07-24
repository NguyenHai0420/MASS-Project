import { Navbar, Container, Nav, NavDropdown } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../../app/providers/AuthProvider";

const AppNavbar = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <Navbar bg="primary" variant="dark" expand="lg" className="shadow-sm">
      <Container>
        <Navbar.Brand as={Link} to="/" className="fw-bold">
          <i className="bi bi-heart-pulse-fill me-2"></i>
          MASS Clinic
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/">Home</Nav.Link>
            {(!user || user.role === "ROLE_PATIENT") && (
              <Nav.Link as={Link} to="/doctors">Danh sách Bác sĩ</Nav.Link>
            )}
            {user && user.role === "ROLE_PATIENT" && (
              <Nav.Link as={Link} to="/my-appointments">Lịch hẹn của tôi</Nav.Link>
            )}
            {user && user.role === "ROLE_ADMIN" && (
              <Nav.Link as={Link} to="/admin/dashboard">Dashboard</Nav.Link>
            )}
            {user && user.role === "ROLE_DOCTOR" && (
              <Nav.Link as={Link} to="/doctor/dashboard">Dashboard</Nav.Link>
            )}
          </Nav>
          <Nav>
            {user ? (
              <NavDropdown title={<span><i className="bi bi-person-circle me-1"></i>{user.fullName || user.email}</span>} id="basic-nav-dropdown">
                <NavDropdown.Item as={Link} to="/profile">My Profile</NavDropdown.Item>
                <NavDropdown.Divider />
                <NavDropdown.Item onClick={handleLogout} className="text-danger">Logout</NavDropdown.Item>
              </NavDropdown>
            ) : (
              <>
                <Nav.Link as={Link} to="/login">Login</Nav.Link>
                <Nav.Link as={Link} to="/register" className="btn btn-light text-primary ms-2 px-3">Register</Nav.Link>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default AppNavbar;
