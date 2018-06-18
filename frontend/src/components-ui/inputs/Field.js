import React from 'react';
import {Input, Message} from 'semantic-ui-react';

export default ({
                    labelStyle,
                    inputStyle,
                    input,
                    label,
                    placeholder,
                    meta,
                }) => {
    return (
        <div>
            <Input
                type="text"
                fluid={true}
                placeholder={placeholder}
                labelPosition="right"
                {...input}
            >
                <input style={inputStyle}/>
                <div style={labelStyle} className="ui label">
                    {label}
                </div>
            </Input>

            {meta.warning && meta.warning.length > 0 ? (<Message style={{
                paddingLeft: '14px',
                paddingTop: '7px',
                paddingBottom: '7px',
                marginTop: '4px',
                marginBottom: '10px'
            }} error list={meta.warning}/>) : null}
            {meta.error && meta.touched ? (<Message style={{
                paddingLeft: '14px',
                paddingTop: '7px',
                paddingBottom: '7px',
                marginTop: '4px',
                marginBottom: '10px'
            }} error content={meta.error}/>) : null}
        </div>
    );
};
