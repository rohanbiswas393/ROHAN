import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Badge, Row, Col, Card, CardHeader, CardBody } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faList, faCheckCircle, faClock } from '@fortawesome/free-solid-svg-icons';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities, getCompletedEntities, getPendingEntities, reset } from './todo.reducer';
import TodoItem from './todo-item';
import TodoForm from './todo-form';

export const Todo = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const navigate = useNavigate();

  const [showForm, setShowForm] = useState(false);
  const [editingTodo, setEditingTodo] = useState(null);
  const [filter, setFilter] = useState('all');

  const todoList = useAppSelector(state => state.todo.entities);
  const loading = useAppSelector(state => state.todo.loading);
  const updateSuccess = useAppSelector(state => state.todo.updateSuccess);

  useEffect(() => {
    dispatch(getEntities());
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      setShowForm(false);
      setEditingTodo(null);
      dispatch(reset());
    }
  }, [updateSuccess]);

  const handleSyncList = () => {
    switch (filter) {
      case 'completed':
        dispatch(getCompletedEntities());
        break;
      case 'pending':
        dispatch(getPendingEntities());
        break;
      default:
        dispatch(getEntities());
    }
  };

  const handleFilterChange = (newFilter: string) => {
    setFilter(newFilter);
    switch (newFilter) {
      case 'completed':
        dispatch(getCompletedEntities());
        break;
      case 'pending':
        dispatch(getPendingEntities());
        break;
      default:
        dispatch(getEntities());
    }
  };

  const handleAddNew = () => {
    setEditingTodo(null);
    setShowForm(true);
  };

  const handleEdit = (todo) => {
    setEditingTodo(todo);
    setShowForm(true);
  };

  const handleCancelForm = () => {
    setShowForm(false);
    setEditingTodo(null);
  };

  const completedCount = todoList.filter(todo => todo.completed).length;
  const pendingCount = todoList.filter(todo => !todo.completed).length;
  const totalCount = todoList.length;

  return (
    <div>
      <h2 id="todo-heading" data-cy="TodoHeading">
        <FontAwesomeIcon icon={faList} className="me-2" />
        My Todos
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh
          </Button>
          <Button color="primary" onClick={handleAddNew}>
            <FontAwesomeIcon icon={faPlus} /> Add New Todo
          </Button>
        </div>
      </h2>

      {/* Statistics Cards */}
      <Row className="mb-4">
        <Col md="4">
          <Card className="text-center">
            <CardBody>
              <FontAwesomeIcon icon={faList} size="2x" className="text-primary mb-2" />
              <h4>{totalCount}</h4>
              <p className="text-muted">Total Todos</p>
            </CardBody>
          </Card>
        </Col>
        <Col md="4">
          <Card className="text-center">
            <CardBody>
              <FontAwesomeIcon icon={faClock} size="2x" className="text-warning mb-2" />
              <h4>{pendingCount}</h4>
              <p className="text-muted">Pending</p>
            </CardBody>
          </Card>
        </Col>
        <Col md="4">
          <Card className="text-center">
            <CardBody>
              <FontAwesomeIcon icon={faCheckCircle} size="2x" className="text-success mb-2" />
              <h4>{completedCount}</h4>
              <p className="text-muted">Completed</p>
            </CardBody>
          </Card>
        </Col>
      </Row>

      {/* Filter Buttons */}
      <Row className="mb-3">
        <Col>
          <div className="btn-group" role="group">
            <Button
              color={filter === 'all' ? 'primary' : 'outline-primary'}
              onClick={() => handleFilterChange('all')}
            >
              All ({totalCount})
            </Button>
            <Button
              color={filter === 'pending' ? 'warning' : 'outline-warning'}
              onClick={() => handleFilterChange('pending')}
            >
              Pending ({pendingCount})
            </Button>
            <Button
              color={filter === 'completed' ? 'success' : 'outline-success'}
              onClick={() => handleFilterChange('completed')}
            >
              Completed ({completedCount})
            </Button>
          </div>
        </Col>
      </Row>

      {/* Todo Form */}
      {showForm && (
        <Card className="mb-4">
          <CardHeader>
            <h5>{editingTodo ? 'Edit Todo' : 'Add New Todo'}</h5>
          </CardHeader>
          <CardBody>
            <TodoForm
              todo={editingTodo}
              onCancel={handleCancelForm}
            />
          </CardBody>
        </Card>
      )}

      {/* Todo List */}
      <div className="table-responsive">
        {todoList && todoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Status</th>
                <th>Title</th>
                <th>Description</th>
                <th>Created</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {todoList.map((todo, i) => (
                <TodoItem
                  key={`entity-${i}`}
                  todo={todo}
                  onEdit={handleEdit}
                />
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning text-center">
              <FontAwesomeIcon icon="info-circle" className="me-2" />
              {filter === 'all' && 'No todos found. Click "Add New Todo" to get started!'}
              {filter === 'pending' && 'No pending todos found.'}
              {filter === 'completed' && 'No completed todos found.'}
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Todo;
