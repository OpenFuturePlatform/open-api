import React from 'react';
import { Divider, Button } from 'semantic-ui-react';
import { EtherscanLink } from '../components-ui/EtherscanLink';
import { ConfirmationModal } from '../components-ui/ConfirmationModal';
import { withVisible } from '../components-ui/withVisible';
import { withSaving } from '../components-ui/withSaving';

export const ScaffoldDeactivateComponent = ({ developerAddress, ...props }) => (
  <span>
    <Button onClick={props.onShow}>Deactivate</Button>
    <ConfirmationModal {...props}>
      <div>
        You are about to deactivate the Scaffold. Are you sure?
        <Divider />
        <span>
          Note: Tokens will be send to Developer Address: <EtherscanLink>{developerAddress}</EtherscanLink>
        </span>
        <Divider />
        <span>PS: Please be patient this may take a while...</span>
      </div>
    </ConfirmationModal>
  </span>
);

export const ScaffoldDeactivate = withVisible(withSaving(ScaffoldDeactivateComponent));
