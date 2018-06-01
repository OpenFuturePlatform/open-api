import React from 'react';
import {Input, Message} from 'semantic-ui-react';
import {debounce} from "lodash";

export default class WrappedInput extends React.Component {

  constructor(props) {
    super(props);

    this.convertToEth = debounce(() => this.props.callback(this.props.formValues), 300);

    this.onChange = (...args) => {
      this.props.input.onChange(...args);
      this.convertToEth();
    };
  }

  render() {
    const {input, type, placeholder, meta, as: As = Input} = this.props;

    return (
      <div>
        <As {...input} value={input.value} type={type} placeholder={placeholder} onChange={this.onChange}/>
        {meta.error && meta.touched ? (<Message style={{
          paddingLeft: '14px',
          paddingTop: '7px',
          paddingBottom: '7px',
          marginTop: '4px',
          marginBottom: '10px'
        }} error content={meta.error}/>) : null}
        {meta.warning ? (<Message style={{
          paddingLeft: '14px',
          paddingTop: '7px',
          paddingBottom: '7px',
          marginTop: '4px',
          marginBottom: '10px'
        }} error content={meta.warning}/>) : null}
      </div>
    );
  }
}