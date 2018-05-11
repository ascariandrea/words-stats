import { fromNullable, none, Option, some } from 'fp-ts/lib/Option';
import * as React from 'react';
import { sendFile } from './api';
import './App.css';
import { IStats } from './models';

interface IState {
  file: Option<File>;
  stats: Option<IStats>;
  error: Option<string>;
}

class App extends React.Component<any, IState> {
  public state: IState = {
    error: none,
    file: none,
    stats: none
  };

  public render() {
    const { file, stats } = this.state;
    return (
      <div className="App">
        <h1 className="App-title">File stats</h1>
        <div>Select a file to upload</div>
        <div className="App-content">
          {file.fold(
            <input type="file" name="document" onChange={this.onChange} />,
            f => <span>{`File ${f.name} loaded!`}</span>
          )}
        </div>
        {stats.fold(null, s => (
          <table style={{ width: '100%' }}>
            <thead>Word stats</thead>
            <tbody>
              {Object.keys(s.words).map(w => (
                <tr key={w}>
                  <td>{w}</td>
                  <td>{s.words[w]}</td>
                </tr>
              ))}
            </tbody>
            <tfoot>
              <tr>
                <td>
                  <b>Total words:</b>
                </td>
                <td>{s.totalWords}</td>
              </tr>
            </tfoot>
          </table>
          ))}
        <button disabled={file.isNone()} onClick={this.onSubmit}>
          Send
        </button>
      </div>
    );
  }

  private onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    // tslint:disable
    this.setState({
      file: fromNullable(e.target.files).chain(fs => fromNullable(fs.item(0))),
      // reset stats and error on file selection
      stats: none,
      error: none
    });
  };

  private onSubmit = () => {
    this.state.file.map(f => {
      sendFile(f)
        .map(s => this.setState({ stats: s }))
        .mapLeft(e => this.setState({ error: some(e.message) }))
        .run();
    });
  };
}

export default App;
