import React from 'react'
import { Routes, Route } from 'react-router-dom'
import ProtectedRoute from './ProtectedRoute.jsx'

import Login from '../pages/auth/Login.jsx'
import Register from '../pages/auth/Register.jsx'
import Dashboard from '../pages/dashboard/Dashboard.jsx'

import EmployeeList from '../pages/employees/EmployeeList.jsx'
import EmployeeDetails from '../pages/employees/EmployeeDetails.jsx'

import DepartmentList from '../pages/departments/DepartmentList.jsx'
import ProjectList from '../pages/projects/ProjectList.jsx'
import AttendancePage from '../pages/attendance/AttendancePage.jsx'
import LeavePage from '../pages/leave/LeavePage.jsx'
import PayrollPage from '../pages/payroll/PayrollPage.jsx'
import PerformancePage from '../pages/performance/PerformancePage.jsx'

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      <Route path="/" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />

      <Route path="/employees" element={<ProtectedRoute><EmployeeList /></ProtectedRoute>} />
      <Route path="/employees/:id" element={<ProtectedRoute><EmployeeDetails /></ProtectedRoute>} />

      <Route path="/departments" element={<ProtectedRoute><DepartmentList /></ProtectedRoute>} />
      <Route path="/projects" element={<ProtectedRoute><ProjectList /></ProtectedRoute>} />
      <Route path="/attendance" element={<ProtectedRoute><AttendancePage /></ProtectedRoute>} />
      <Route path="/leave" element={<ProtectedRoute><LeavePage /></ProtectedRoute>} />
      <Route path="/payroll" element={<ProtectedRoute roles={['ADMIN', 'HR']}><PayrollPage /></ProtectedRoute>} />
      <Route path="/performance" element={<ProtectedRoute><PerformancePage /></ProtectedRoute>} />
    </Routes>
  )
}
