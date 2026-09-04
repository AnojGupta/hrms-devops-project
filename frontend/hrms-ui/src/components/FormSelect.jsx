import React from 'react'

export default function FormSelect({ label, error, children, ...selectProps }) {
  return (
    <div className="form-group">
      {label && <label>{label}</label>}
      <select className="form-control" {...selectProps}>
        {children}
      </select>
      {error && <div className="error-text">{error}</div>}
    </div>
  )
}
