import axios, { AxiosError, AxiosResponse } from 'axios';
import { left, right } from 'fp-ts/lib/Either';
import { fromNullable, Option } from 'fp-ts/lib/Option';
import { Task } from 'fp-ts/lib/Task';
import { TaskEither } from 'fp-ts/lib/TaskEither';
import config from './config';
import { IStats } from './models';

const client = axios.create({
  baseURL: config.baseURL
});

export const sendFile = (file: File): TaskEither<AxiosError, Option<IStats>> =>
  new TaskEither(
    new Task(() =>
      client
        .post('/fiileStats', file)
        .then((r: AxiosResponse<{ data: IStats }>) =>
          right(fromNullable(r.data.data))
        )
        .catch(e => left(e))
    )
  );
