import { Container, Row, Col, Card, Table, Badge, Button } from "react-bootstrap";

import doctorsData from "../data/doctorsData";

export default function DoctorDetail() {

  const doctor = doctorsData[0];

  return (
    <Container fluid className="p-4">
      <h3 className="mb-4">Doctor Details</h3>

      {/* Stats */}
      <Row className="mb-4">
        <Col md={3}>
          <Card body>
            <strong>Experience:</strong>
            <br />
            {doctor.experience}
          </Card>
        </Col>

        <Col md={3}>
          <Card body>
            <strong>Specialty</strong>
            <br />
            {doctor.specialty}
          </Card>
        </Col>

        <Col md={3}>
          <Card body>
            <strong>Status</strong>
            <br />
            {doctor.status}
          </Card>
        </Col>

        <Col md={3}>
          <Card body>
            <strong>Email</strong>
            <br />
            {doctor.email}
          </Card>
        </Col>
      </Row>

      {/* Actions */}
      <Row className="mb-3">
        <Col className="text-end">
          <Button variant="warning" className="me-2">
            Edit
          </Button>

          <Button
  variant="secondary"
  onClick={() => window.history.back()}
>
  Back
</Button>
        </Col>
      </Row>

      {/* Doctor Information */}
      <Card>
        <Card.Body>
          <Table bordered responsive>
            <tbody>
              <tr>
                <th width="250">Doctor Name</th>
                <td>{doctor.name}</td>
              </tr>

              <tr>
                <th>Specialty</th>
                <td>{doctor.specialty}</td>
              </tr>

              <tr>
                <th>Email</th>
                <td>{doctor.email}</td>
              </tr>

              <tr>
                <th>Phone</th>
                <td>{doctor.phone}</td>
              </tr>

              <tr>
                <th>Experience</th>
                <td>{doctor.experience}</td>
              </tr>

              <tr>
                <th>Status</th>
                <td>
                  <Badge
                    bg={
                      doctor.status === "Active"
                        ? "success"
                        : doctor.status === "On Leave"
                        ? "warning"
                        : "secondary"
                    }
                  >
                    {doctor.status}
                  </Badge>
                </td>
              </tr>
            </tbody>
          </Table>
        </Card.Body>
      </Card>

      {/* Recent Appointments */}
      <Card className="mt-4">
        <Card.Body>
          <h5 className="mb-3">Recent Appointments</h5>

          <Table hover responsive>
            <thead>
              <tr>
                <th>Patient</th>
                <th>Date</th>
                <th>Time</th>
                <th>Status</th>
              </tr>
            </thead>

            <tbody>
              <tr>
                <td>Nguyen Van A</td>
                <td>05/06/2026</td>
                <td>09:00</td>
                <td>
                  <Badge bg="success">Completed</Badge>
                </td>
              </tr>

              <tr>
                <td>Tran Thi B</td>
                <td>06/06/2026</td>
                <td>14:00</td>
                <td>
                  <Badge bg="warning">Pending</Badge>
                </td>
              </tr>
            </tbody>
          </Table>
        </Card.Body>
      </Card>
    </Container>
  );
}