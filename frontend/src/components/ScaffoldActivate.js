import React from 'react';
import { withVisible } from '../components-ui/withVisible';
import { Button, Divider } from 'semantic-ui-react';
import { withSaving } from '../components-ui/withSaving';
import { ConfirmationModal } from '../components-ui/ConfirmationModal';
import styled from 'styled-components';
import { t } from '../utils/messageTexts';

const ErrorMessage = styled.div`
  display: inline-block;
  padding-left: 30px;
  color: red;
`;

const ScaffoldActivateComponent = ({ children, ...props }) => (
  <div>
    <span>
      <Button primary onClick={props.onShow}>
        Activate
      </Button>
      <ConfirmationModal {...props}>
        <div>
          {t('sure to activate scaffold')}
          <Divider />
          <span>{t('it may take a while')}</span>
        </div>
      </ConfirmationModal>
    </span>
    <ErrorMessage>{t('scaffold not activated')}</ErrorMessage>
  </div>
);

export const ScaffoldActivate = withVisible(withSaving(ScaffoldActivateComponent));
