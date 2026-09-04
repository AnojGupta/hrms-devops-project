import React from 'react'

export default function FormInput({ label, error, ...inputProps }) {
  return (
    <div className="form-group">
      {label && <label>{label}</label>}
      <input className="form-control" {...inputProps} />
      {error && <div className="error-text">{error}</div>}
    </div>
  )
}
