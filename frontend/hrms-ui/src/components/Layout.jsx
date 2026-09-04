import React from 'react'
import { NavLink } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

const NAV_ITEMS = [
  { path: '/', label: 'Dashboard', end: true },
  { path: '/employees', label: 'Employees' },
  { path: '/departments', label: 'Departments' },
  { path: '/projects', label: 'Projects' },
  { path: '/attendance', label: 'Attendance' },
  { path: '/leave', label: 'Leave' },
  { path: '/payroll', label: 'Payroll', roles: ['ADMIN', 'HR'] },
  { path: '/performance', label: 'Performance' },
]

export default function Layout({ children }) {
  const { user, logout, hasRole } = useAuth()

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">HRMS</div>
        <nav>
          {NAV_ITEMS.filter((item) => !item.roles || hasRole(...item.roles)).map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              end={item.end}
              className={({ isActive }) => (isActive ? 'active' : '')}
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
        <button className="btn btn-secondary logout-btn" onClick={logout}>
          Log out {user ? `(${user.username})` : ''}
        </button>
      </aside>
      <main className="main-content">{children}</main>
    </div>
  )
}
