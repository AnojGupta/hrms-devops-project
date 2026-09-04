import React from 'react'

export default function EmptyState({ message = 'No records found.' }) {
  return <div className="empty-state">{message}</div>
}
