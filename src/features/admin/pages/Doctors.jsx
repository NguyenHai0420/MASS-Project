import { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Table,
  Badge,
  Button,
  Form,
} from "react-bootstrap";

import doctorsData from "../data/doctorsData";

export default function Doctors() {
  const [doctors, setDoctors] = useState([]);
  // const [loading, setLoading] = useState(true);

  useEffect(() => {
    setTimeout(() => {
      setDoctors(doctorsData);
      // setLoading(false);
    }, 500);
  }, []);

  return (
    <Container fluid className="p-4">
      <h3 className="mb-4">Doctors</h3>

      {/* 🔹 Stats */}
      <Row className="mb-4">
        <Col md={3}><Card body>Total Doctors: 32</Card></Col>
        <Col md={3}><Card body>Active: 28</Card></Col>
        <Col md={3}><Card body>On Leave: 2</Card></Col>
        <Col md={3}><Card body>Inactive: 2</Card></Col>
      </Row>

      {/* 🔹 Filter */}
      <Row className="mb-3">
        <Col md={4}>
          <Form.Control placeholder="Search doctor..." />
        </Col>
        <Col md={3}>
          <Form.Select>
            <option>All Specialties</option>
          </Form.Select>
        </Col>
        <Col md={3}>
          <Form.Select>
            <option>All Status</option>
          </Form.Select>
        </Col>
        <Col md={2}>
          <Button variant="success" className="w-100">
            + Add Doctor
          </Button>
        </Col>
      </Row>

      {/* 🔹 Loading */}
      {/* {loading && <p>Loading...</p>} */}

      {/* 🔹 Table */}
      {/* {!loading && ( */}
        <Card>
          <Card.Body>
            <Table hover responsive>
              <thead>
                <tr>
                  <th>Doctor</th>
                  <th>Specialty</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>Experience</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>

              <tbody>
                {doctors.map((doc, index) => (
                  <tr key={index}>
                    <td>{doc.name}</td>
                    <td>{doc.specialty}</td>
                    <td>{doc.email}</td>
                    <td>{doc.phone}</td>
                    <td>{doc.experience}</td>

                    <td>
                      <Badge
                        bg={
                          doc.status === "Active"
                            ? "success"
                            : doc.status === "On Leave"
                            ? "warning"
                            : "secondary"
                        }
                      >
                        {doc.status}
                      </Badge>
                    </td>

                    <td>
                      <Button
                        size="sm"
                        variant="outline-primary"
                        className="me-2"
                      >
                        View
                      </Button>
                      <Button size="sm" variant="outline-warning">
                        Edit
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Card.Body>
        </Card>
      {/* )} */}
    </Container>
  );
}