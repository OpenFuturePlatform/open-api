// ScaffoldField contains logic to render a single field
import React from 'react';
import {Button, Input} from 'semantic-ui-react';
import CopyToClipboardButton from './CopyToClipboardButton';

export default ({
                    action,
                    styleButton,
                    inputStyle,
                    input,
                    callback,
                    formValues,
                    placeholder,
                    callbackArguments,
                    disable,
                    disableInput,
                }) => {
    return (
        <Input
            placeholder={placeholder}
            type="text"
            fluid={true}
            action
            {...input}
            style={inputStyle}
        >
            <input/>
            {input.value ? <CopyToClipboardButton text={input.value}/> :
                <Button
                    style={styleButton}
                    disabled={disable}
                    type="button"
                    onClick={() => {
                        callback(formValues, callbackArguments);
                    }}
                >
                    {action}
                </Button>
            }
        </Input>
    );
};
