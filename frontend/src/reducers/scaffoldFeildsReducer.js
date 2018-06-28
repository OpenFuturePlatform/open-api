import {GET_SCAFFOLD_FIELDS, LOAD_SCAFFOLD_FIELDS} from '../actions/types';

const initialState = {
  currency: 'USD',
  properties: []

  // conversionAmount: 2.0346416588,
  // currency: "USD",
  // description: "hello " + Math.round(Math.random()*1000),
  // developerAddress: "",
  // fiatAmount: "555",
  // webHook: 'google.com',
  // properties: [{
  //   defaultValue: "1",
  //   name: "prop1",
  //   type: "NUMBER"
  // }],
};

export default function (state = initialState, action) {
  const {payload, type} = action;
  switch (type) {
    case LOAD_SCAFFOLD_FIELDS:
      return {...state, ...action.payload};
    case GET_SCAFFOLD_FIELDS:
      return payload;
    default:
      return state;
  }
}
