import { fromNullable, none, Option } from 'fp-ts/lib/Option';
import * as React from 'react';
import './App.css';

interface IState {
  file: Option<File>;
}

class App extends React.Component<any, IState> {
  public state: IState = {
    file: none
  };
  public render() {
    const { file } = this.state;
    return (
      <div className="App">
        <h1 className="App-title">File stats</h1>
        <div>Select a file to upload</div>
        {file.fold(
          <input
            className="App-input"
            type="file"
            name="document"
            onChange={this.onChange}
          />,
          f => <div>{`File ${f.name} loaded!`}</div>
        )}
      </div>
    );
  }

  private onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    // tslint:disable
    this.setState({
      file: fromNullable(e.target.files).chain(fs => fromNullable(fs.item(0)))
    });
  };
}

export default App;
