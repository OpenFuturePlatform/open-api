import React from 'react';
import { Field } from 'redux-form';
import { Button, Grid, Input, Message } from 'semantic-ui-react';
import { DropdownField } from 'react-semantic-redux-form';
import ScaffoldPropertyField from './PropertyField';
import { MAX_CONTRACT_PROPERTIES_COUNT } from '../../const/index';

const renderRemoveButton = (index, fields, onRemove) => {
  if (index === 0 && fields.length === 1) {
    return null;
  }
  return (
    <Button
      icon="remove circle"
      type="button"
      onClick={onRemove}
      style={{
        borderTopLeftRadius: '0px',
        borderBottomLeftRadius: '0px',
        maxWidth: '40px'
      }}
    />
  );
};

const renderTypeField = (field, type) => {
  if (type === 'BOOLEAN') {
    return (
      <Field
        name={`${field}.defaultValue`}
        component={DropdownField}
        options={[{ key: 'true', text: 'true', value: 'true' }, { key: 'false', text: 'false', value: 'false' }]}
        className="ui selection dropdown inputField"
        placeholder="Default Value"
        fluid
      />
    );
  }

  return (
    <Field
      name={`${field}.defaultValue`}
      inputStyle={{
        borderTopLeftRadius: '0px',
        borderTopRightRadius: '0px',
        borderBottomRightRadius: '0px',
        borderBottomLeftRadius: '0px'
      }}
      component={ScaffoldPropertyField}
      placeholder="Default Value"
      labelPosition="right"
      className={'inputField'}
      fluid
    />
  );
};

const renderErrorMessage = list => {
  if (!list || list.length <= 0) {
    return null;
  }
  return (
    <Message
      error
      list={list}
      style={{
        paddingLeft: '14px',
        paddingTop: '7px',
        paddingBottom: '7px',
        marginTop: '4px',
        marginBottom: '10px'
      }}
    />
  );
};

export default ({ fields, scaffoldFieldsErrors, scaffoldProperties = [] }) => {
  const isMaxPropertiesCount = fields.length >= MAX_CONTRACT_PROPERTIES_COUNT;

  if (!fields.length) {
    fields.push({});
  }

  const handleOnAddProperty = () => {
    if (!isMaxPropertiesCount) {
      fields.push({});
    }
  };

  const renderProperties = fields.map((field, index) => {
    const scaffoldProperty = scaffoldProperties[index] || {};
    const scaffoldPropertyType = scaffoldProperty.type || '';
    const scaffoldPropertyErrors = scaffoldFieldsErrors[index];

    return (
      <div key={index}>
        <Input fluid={true} className={'inputContainer'}>
          <Field
            name={`${field}.name`}
            component={ScaffoldPropertyField}
            className={'inputField'}
            labelPosition="right"
            placeholder="Scaffold Property Name"
            fluid
          />
          <Field
            name={`${field}.type`}
            style={{
              borderTopLeftRadius: '0px',
              borderTopRightRadius: '0px',
              borderBottomRightRadius: '0px',
              borderBottomLeftRadius: '0px',
              width: '200px'
            }}
            component={DropdownField}
            placeholder="Datatype"
            options={[
              { key: 'string', text: 'string', value: 'STRING' },
              { key: 'number', text: 'number', value: 'NUMBER' },
              { key: 'boolean', text: 'boolean', value: 'BOOLEAN' }
            ]}
            className="ui selection"
          />
          <div>{renderTypeField(field, scaffoldPropertyType)}</div>
          {renderRemoveButton(index, fields, () => fields.remove(index))}
        </Input>
        {renderErrorMessage(scaffoldPropertyErrors)}
      </div>
    );
  });

  return (
    <div>
      <div style={{ height: '30px' }}>
        <Button
          primary
          disabled={isMaxPropertiesCount}
          type="button"
          floated="right"
          onClick={handleOnAddProperty}
          style={{
            marginBottom: '10px',
            marginRight: '15px',
            backgroundColor: '#3193F5'
          }}
        >
          {isMaxPropertiesCount ? `Max Property Count = ${MAX_CONTRACT_PROPERTIES_COUNT}` : 'Add Scaffold Property'}
        </Button>
      </div>
      <div style={{ marginBottom: '10px' }}>
        Properties to be recorded Onchain and sent to developer through Open API
      </div>
      <Grid.Row style={{ display: 'block' }}>{renderProperties}</Grid.Row>
    </div>
  );
};
