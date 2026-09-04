import React, { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { getEmployee } from '../../api/employeeApi'
import { getAttendanceByEmployee } from '../../api/attendanceApi'
import { getLeaves } from '../../api/leaveApi'
import { getPayrollByEmployee } from '../../api/payrollApi'
import { getReviewsByEmployee } from '../../api/performanceApi'
import Loader from '../../components/Loader.jsx'
import ErrorMessage from '../../components/ErrorMessage.jsx'
import StatusBadge from '../../components/StatusBadge.jsx'

export default function EmployeeDetails() {
  const { id } = useParams()
  const [employee, setEmployee] = useState(null)
  const [attendance, setAttendance] = useState([])
  const [leaves, setLeaves] = useState([])
  const [payroll, setPayroll] = useState([])
  const [reviews, setReviews] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    setLoading(true)
    Promise.all([
      getEmployee(id),
      getAttendanceByEmployee(id),
      getLeaves(),
      getPayrollByEmployee(id),
      getReviewsByEmployee(id),
    ])
      .then(([empRes, attRes, leaveRes, payRes, revRes]) => {
        setEmployee(empRes.data)
        setAttendance(attRes.data)
        setLeaves(leaveRes.data.filter((l) => l.employee?.id === Number(id)))
        setPayroll(payRes.data)
        setReviews(revRes.data)
      })
      .catch((err) => setError(err.response?.data?.message || 'Failed to load employee details'))
      .finally(() => setLoading(false))
  }, [id])

  if (loading) return <Loader />
  if (error) return <ErrorMessage message={error} />
  if (!employee) return null

  return (
    <div>
      <div className="page-header">
        <h1>{employee.firstName} {employee.lastName}</h1>
        <Link to="/employees" className="btn btn-secondary">Back to list</Link>
      </div>

      <div className="grid" style={{ gridTemplateColumns: '1fr 1fr', marginBottom: 20 }}>
        <div className="card">
          <h3 style={{ marginTop: 0 }}>Profile</h3>
          <p><strong>Employee Code:</strong> {employee.employeeCode}</p>
          <p><strong>Email:</strong> {employee.email}</p>
          <p><strong>Phone:</strong> {employee.phoneNumber || '-'}</p>
          <p><strong>Job Title:</strong> {employee.jobTitle || '-'}</p>
          <p><strong>Department:</strong> {employee.department?.name || '-'}</p>
          <p><strong>Manager:</strong> {employee.manager ? `${employee.manager.firstName} ${employee.manager.lastName}` : '-'}</p>
          <p><strong>Status:</strong> <StatusBadge status={employee.employmentStatus} /></p>
          <p><strong>Joining Date:</strong> {employee.joiningDate || '-'}</p>
          <p><strong>Salary:</strong> {employee.salary ?? '-'}</p>
        </div>
        <div className="card">
          <h3 style={{ marginTop: 0 }}>Address</h3>
          {employee.address ? (
            <>
              <p>{employee.address.street}</p>
              <p>{employee.address.city}, {employee.address.state}</p>
              <p>{employee.address.country} {employee.address.postalCode}</p>
            </>
          ) : (
            <p>No address on file.</p>
          )}
        </div>
      </div>

      <Section title="Recent Attendance">
        {attendance.length === 0 ? <p>No attendance records.</p> : (
          <table>
            <thead><tr><th>Date</th><th>Check In</th><th>Check Out</th><th>Status</th></tr></thead>
            <tbody>
              {attendance.map((a) => (
                <tr key={a.id}>
                  <td>{a.date}</td>
                  <td>{a.checkInTime || '-'}</td>
                  <td>{a.checkOutTime || '-'}</td>
                  <td><StatusBadge status={a.status} /></td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Section>

      <Section title="Leave History">
        {leaves.length === 0 ? <p>No leave requests.</p> : (
          <table>
            <thead><tr><th>Type</th><th>From</th><th>To</th><th>Status</th></tr></thead>
            <tbody>
              {leaves.map((l) => (
                <tr key={l.id}>
                  <td>{l.leaveType?.replace(/_/g, ' ')}</td>
                  <td>{l.startDate}</td>
                  <td>{l.endDate}</td>
                  <td><StatusBadge status={l.status} /></td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Section>

      <Section title="Payroll">
        {payroll.length === 0 ? <p>No payroll records.</p> : (
          <table>
            <thead><tr><th>Basic</th><th>Bonus</th><th>Deductions</th><th>Net Salary</th><th>Status</th></tr></thead>
            <tbody>
              {payroll.map((p) => (
                <tr key={p.id}>
                  <td>{p.basicSalary}</td>
                  <td>{p.bonus}</td>
                  <td>{p.deductions}</td>
                  <td><strong>{p.netSalary}</strong></td>
                  <td><StatusBadge status={p.paymentStatus} /></td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Section>

      <Section title="Performance Reviews">
        {reviews.length === 0 ? <p>No performance reviews.</p> : (
          <table>
            <thead><tr><th>Reviewer</th><th>Rating</th><th>Feedback</th><th>Date</th></tr></thead>
            <tbody>
              {reviews.map((r) => (
                <tr key={r.id}>
                  <td>{r.reviewer ? `${r.reviewer.firstName} ${r.reviewer.lastName}` : '-'}</td>
                  <td>{r.rating} / 5</td>
                  <td>{r.feedback || '-'}</td>
                  <td>{r.reviewDate}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Section>
    </div>
  )
}

function Section({ title, children }) {
  return (
    <div className="card" style={{ marginBottom: 20 }}>
      <h3 style={{ marginTop: 0 }}>{title}</h3>
      {children}
    </div>
  )
}
