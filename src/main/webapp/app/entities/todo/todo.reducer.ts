import axios from 'axios';
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITodo>,
  entity: {} as Readonly<ITodo>,
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/todos';

// Define the Todo interface
export interface ITodo {
  id?: string;
  title?: string;
  description?: string;
  completed?: boolean;
  userLogin?: string;
  userId?: string;
  createdDate?: string;
  lastModifiedDate?: string;
}

export type TodoState = Readonly<typeof initialState>;

// Actions

export const getEntities = createAsyncThunk('todo/fetch_entity_list', async () => {
  const requestUrl = `${apiUrl}`;
  return axios.get<ITodo[]>(requestUrl);
});

export const getCompletedEntities = createAsyncThunk('todo/fetch_completed_entity_list', async () => {
  const requestUrl = `${apiUrl}/completed`;
  return axios.get<ITodo[]>(requestUrl);
});

export const getPendingEntities = createAsyncThunk('todo/fetch_pending_entity_list', async () => {
  const requestUrl = `${apiUrl}/pending`;
  return axios.get<ITodo[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'todo/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ITodo>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'todo/create_entity',
  async (entity: ITodo, thunkAPI) => {
    const result = await axios.post<ITodo>(apiUrl, entity);
    thunkAPI.dispatch(getEntities());
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'todo/update_entity',
  async (entity: ITodo, thunkAPI) => {
    const result = await axios.put<ITodo>(`${apiUrl}/${entity.id}`, entity);
    thunkAPI.dispatch(getEntities());
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'todo/partial_update_entity',
  async (entity: ITodo, thunkAPI) => {
    const result = await axios.patch<ITodo>(`${apiUrl}/${entity.id}`, entity);
    thunkAPI.dispatch(getEntities());
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const toggleEntity = createAsyncThunk(
  'todo/toggle_entity',
  async (id: string | number, thunkAPI) => {
    const result = await axios.put<ITodo>(`${apiUrl}/${id}/toggle`);
    thunkAPI.dispatch(getEntities());
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'todo/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete(requestUrl);
    thunkAPI.dispatch(getEntities());
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const TodoSlice = createSlice({
  name: 'todo',
  initialState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(
        action => action.type.endsWith('/pending'),
        state => {
          state.errorMessage = null;
          state.updateSuccess = false;
          state.loading = true;
        }
      )
      .addMatcher(
        action => action.type.endsWith('/rejected'),
        (state, action) => {
          state.loading = false;
          state.updating = false;
          state.updateSuccess = false;
          state.errorMessage = action.error.message;
        }
      )
      .addMatcher(
        action => action.type.includes('fetch_entity_list') && action.type.endsWith('/fulfilled'),
        (state, action) => {
          state.loading = false;
          state.entities = action.payload.data;
          state.totalItems = action.payload.data.length;
        }
      )
      .addMatcher(
        action => (action.type.includes('create_entity') || action.type.includes('update_entity') || action.type.includes('toggle_entity')) && action.type.endsWith('/fulfilled'),
        (state, action) => {
          state.updating = false;
          state.loading = false;
          state.updateSuccess = true;
          state.entity = action.payload.data;
        }
      );
  },
});

export const { reset } = TodoSlice.actions;

// Reducer
export default TodoSlice.reducer;
