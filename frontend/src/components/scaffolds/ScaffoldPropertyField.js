import React from 'react';
import {Input} from 'semantic-ui-react';

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
                placeholder={placeholder}
                {...input}
            >
                <input/>
            </Input>
        </div>
    );
};
