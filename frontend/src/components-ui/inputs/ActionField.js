import React from 'react';
import {Input} from 'semantic-ui-react';

export default ({
                  action,
                  styleButton,
                  inputStyle,
                  input,
                  placeholder,
                  callbackArguments,
                  disable,
                  disableInput,
                  labelStyle
                }) => {
  return (
    <Input
      placeholder={placeholder}
      labelPosition="right"
      type="text"
      fluid={true}
      action
      {...input}
      style={inputStyle}
    >
      <input disabled={disable || disableInput}/>
      <div style={labelStyle} className="ui label">
        {action}
      </div>
    </Input>
  );
};
