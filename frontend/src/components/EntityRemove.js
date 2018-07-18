import React from 'react';
import { withVisible } from '../components-ui/withVisible';
import { Icon } from 'semantic-ui-react';
import { withSaving } from '../components-ui/withSaving';
import { ConfirmationModal } from '../components-ui/ConfirmationModal';

const EntityRemoveComponent = ({ children, ...props }) => (
  <span>
    <Icon link name="remove" size="large" onClick={props.onShow} />
    <ConfirmationModal {...props}>{children}</ConfirmationModal>
  </span>
);

export const EntityRemove = withVisible(withSaving(EntityRemoveComponent));
