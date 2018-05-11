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

export const sendFile = (
  file: File
): TaskEither<AxiosError, Option<IStats>> => {
  const form = new FormData();
  form.append('doc', file);

  return new TaskEither(
    new Task(() =>
      client
        .post('/fileStats', form, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then((r: AxiosResponse<IStats>) => right(fromNullable(r.data)))
        .catch(e => left(e))
    )
  );
};
