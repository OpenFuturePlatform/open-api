import React from 'react';
import {Field} from 'redux-form';
import {Button, Grid, Input, Message} from 'semantic-ui-react';
import {DropdownField} from 'react-semantic-redux-form';

export default ({fields, datatypeOptions, scaffoldFieldsErrors, formValues}) => {
    return (
        <div>
            <div style={{height: '30px'}}>
                <Button
                    primary
                    style={{
                        marginBottom: '10px',
                        marginRight: '15px',
                        backgroundColor: '#3193F5',
                    }}
                    type="button"
                    onClick={() => fields.push({})}
                    floated="right"
                >
                    Add Scaffold Property
                </Button>
            </div>
            <div style={{marginBottom: '10px'}}>
                Properties to be recorded Onchain and sent to developer through
                Open API
            </div>
            <Grid.Row style={{display: 'block'}}>
                {fields.map((property, index) => {
                    return (
                        <div key={index}>
                            <Input
                                fluid={true}
                                className={'inputContainer'}
                            >
                                <Field
                                    name={`${property}.name`}
                                    component={Input}
                                    className={'inputField'}
                                    labelPosition="right"
                                    placeholder="Scaffold Property Name"
                                />
                                <Field
                                    style={{
                                        borderTopLeftRadius: '0px',
                                        borderTopRightRadius: '0px',
                                        borderBottomRightRadius: '0px',
                                        borderBottomLeftRadius: '0px',
                                        width: '200px'
                                    }}
                                    name={`${property}.type`}
                                    component={DropdownField}
                                    placeholder="Datatype"
                                    options={datatypeOptions}
                                    className="ui selection"
                                />
                                {formValues.scaffoldFields[index].datatype === 'bool' ?
                                    <div style={{width: '65%'}}>
                                        <Field
                                            style={{
                                                borderTopLeftRadius: '0px',
                                                borderTopRightRadius: '0px',
                                                borderBottomRightRadius: '0px',
                                                borderBottomLeftRadius: '0px',
                                                width: '65% !important'
                                            }}
                                            name={`${property}.defaultValue`}
                                            component={DropdownField}
                                            options={[
                                                {key: 'true', text: 'true', value: 'true'},
                                                {key: 'false', text: 'false', value: 'false'},
                                            ]}
                                            className="ui selection dropdown"
                                            placeholder="Default Value"
                                            fluid
                                        />
                                    </div> :
                                    <Field
                                        style={{
                                            borderTopLeftRadius: '0px',
                                            borderTopRightRadius: '0px',
                                            borderBottomRightRadius: '0px',
                                            borderBottomLeftRadius: '0px'
                                        }}
                                        name={`${property}.defaultValue`}
                                        component={Input}
                                        placeholder="Default Value"
                                        labelPosition="right"
                                        className={'inputField'}
                                    />
                                }
                                <Button
                                    style={{
                                        borderTopLeftRadius: '0px',
                                        borderBottomLeftRadius: '0px',
                                        maxWidth: '40px'
                                    }}
                                    icon="remove circle"
                                    type="button"
                                    onClick={() => {
                                        fields.remove(index);
                                    }}
                                />
                            </Input>
                            {((formValues && formValues.scaffoldFields[index]) && (scaffoldFieldsErrors[index].length > 0)) ? (
                                <Message style={{
                                    paddingLeft: '14px',
                                    paddingTop: '7px',
                                    paddingBottom: '7px',
                                    marginTop: '4px',
                                    marginBottom: '10px'
                                }} error list={scaffoldFieldsErrors[index]}/>) : null}
                        </div>
                    );
                })}
            </Grid.Row>
        </div>
    );
};
