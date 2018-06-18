import React from 'react';
import {Button, Icon} from 'semantic-ui-react';
import {CopyToClipboard} from 'react-copy-to-clipboard';

export default ({text}) => {
    return (
        <CopyToClipboard text={text}>
            <Button type="button"><Icon name="copy"></Icon>Copy to Clipboard</Button>
        </CopyToClipboard>
    );
};