import React, { useContext } from 'react';
import { Navbar, Nav, Container, Button } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../app/providers/AuthProvider';

const PatientNavbar = () => {
  const navigate = useNavigate();
  const { user, logout } = useContext(AuthContext);

  const handleLogout = async () => {
    await logout();
  };

  return (
    <Navbar bg="primary" variant="dark" expand="lg" sticky="top">
      <Container>
        <Navbar.Brand as={Link} to="/home">MASS Clinic</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/home">Trang chủ</Nav.Link>
            <Nav.Link as={Link} to="/doctors">Danh sách Bác sĩ</Nav.Link>
            <Nav.Link as={Link} to="/my-appointments">Lịch của tôi</Nav.Link>
          </Nav>
          <Nav>
            {user ? (
              <Button variant="outline-light" onClick={handleLogout}>Đăng xuất</Button>
            ) : (
              <>
                <Button variant="outline-light" as={Link} to="/login" className="me-2">Đăng nhập</Button>
                <Button variant="light" as={Link} to="/register">Đăng ký</Button>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default PatientNavbar;
