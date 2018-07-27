import React from 'react';
import { Divider, Button } from 'semantic-ui-react';
import { EtherscanLink } from '../components-ui/EtherscanLink';
import { ConfirmationModal } from '../components-ui/ConfirmationModal';
import { withVisible } from '../components-ui/withVisible';
import { withSaving } from '../components-ui/withSaving';
import { t } from '../utils/messageTexts';

export const ScaffoldDeactivateComponent = ({ developerAddress, ...props }) => (
  <span>
    <Button onClick={props.onShow}>Deactivate</Button>
    <ConfirmationModal {...props}>
      <div>
        {t('sure to deactivate scaffold')}
        <Divider />
        <span>
          {t('tokens will be returned to dev address')} <EtherscanLink>{developerAddress}</EtherscanLink>
        </span>
        <Divider />
        <span>{t('it may take a while')}</span>
      </div>
    </ConfirmationModal>
  </span>
);

export const ScaffoldDeactivate = withVisible(withSaving(ScaffoldDeactivateComponent));
