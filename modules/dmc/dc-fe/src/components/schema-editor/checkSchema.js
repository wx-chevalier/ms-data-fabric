function checkSubSchema(schema, parent, state) {
  let prop = state.property;
  if (prop) contextAppend(state.options, prop);
  if (state.options.lint) state.options.linter('schema', schema, 'schema', state.options);
  schema.should.be.an.Object();
  schema.should.not.be.an.Array();

  if (typeof schema.$ref !== 'undefined') {
    schema.$ref.should.be.a.String();
    if (state.options.lint) state.options.linter('reference', schema, '$ref', state.options);
    if (prop) state.options.context.pop();
    return; // all other properties SHALL be ignored
  }

  for (let k in schema) {
    if (!k.startsWith('x-')) {
      should(
        [
          'type',
          'items',
          'format',
          'properties',
          'required',
          'minimum',
          'maximum',
          'exclusiveMinimum',
          'exclusiveMaximum',
          'enum',
          'default',
          'description',
          'title',
          'readOnly',
          'writeOnly',
          'anyOf',
          'allOf',
          'oneOf',
          'not',
          'discriminator',
          'maxItems',
          'minItems',
          'additionalItems',
          'additionalProperties',
          'example',
          'maxLength',
          'minLength',
          'pattern',
          'uniqueItems',
          'xml',
          'externalDocs',
          'nullable',
          'deprecated',
          'minProperties',
          'maxProperties',
          'multipleOf'
        ].indexOf(k)
      ).be.greaterThan(-1, 'Schema object cannot have additionalProperty: ' + k);
    }
  }

  if (schema.multipleOf) {
    schema.multipleOf.should.be.a.Number();
    schema.multipleOf.should.be.greaterThan(0);
  }
  if (schema.maximum) {
    schema.maximum.should.be.a.Number();
  }
  if (schema.exclusiveMaximum) {
    schema.exclusiveMaximum.should.be.a.Boolean();
  }
  if (schema.minimum) {
    schema.minimum.should.be.a.Number();
  }
  if (schema.exclusiveMinimum) {
    schema.exclusiveMinimum.should.be.a.Boolean();
  }
  if (schema.maxLength) {
    schema.maxLength.should.be.a.Number();
    schema.maxLength.should.be.greaterThan(-1);
  }
  if (schema.minLength) {
    schema.minLength.should.be.a.Number();
    schema.minLength.should.be.greaterThan(-1);
  }
  if (schema.pattern) {
    try {
      let regex = new RegExp(schema.pattern);
    } catch (ex) {
      should.fail(false, true, 'pattern does not conform to ECMA-262');
    }
  }
  if (typeof schema.items !== 'undefined') {
    schema.items.should.be.an.Object();
    schema.items.should.not.be.an.Array();
  }
  if (schema.additionalItems) {
    if (typeof schema.additionalItems === 'boolean') {
    } else if (typeof schema.additionalItems === 'object') {
      schema.additionalItems.should.not.be.an.Array();
    } else should.fail(false, true, 'additionalItems must be a boolean or schema');
  }
  if (schema.additionalProperties) {
    if (typeof schema.additionalProperties === 'boolean') {
    } else if (typeof schema.additionalProperties === 'object') {
      schema.additionalProperties.should.not.be.an.Array();
    } else should.fail(false, true, 'additionalProperties must be a boolean or schema');
  }
  if (schema.maxItems) {
    schema.maxItems.should.be.a.Number();
    schema.maxItems.should.be.greaterThan(-1);
  }
  if (schema.minItems) {
    schema.minItems.should.be.a.Number();
    schema.minItems.should.be.greaterThan(-1);
  }
  if (typeof schema.uniqueItems !== 'undefined') {
    schema.uniqueItems.should.be.a.Boolean();
  }
  if (schema.maxProperties) {
    schema.maxProperties.should.be.a.Number();
    schema.maxProperties.should.be.greaterThan(-1);
  }
  if (schema.minProperties) {
    schema.minProperties.should.be.a.Number();
    schema.minProperties.should.be.greaterThan(-1);
  }
  if (typeof schema.required !== 'undefined') {
    schema.required.should.be.an.Array();
    schema.required.should.not.be.empty();
    common.hasDuplicates(schema.required).should.be.exactly(false, 'required items must be unique');
  }
  if (schema.properties) {
    schema.properties.should.be.an.Object();
    schema.properties.should.not.be.an.Array();
  }
  schema.should.not.have.property('patternProperties');
  /*if (schema.patternProperties) {
      schema.patternProperties.should.be.an.Object();
      for (let prop in schema.patternProperties) {
          try {
              let regex = new RegExp(prop);
          }
          catch (ex) {
              should.fail(false,true,'patternProperty '+prop+' does not conform to ECMA-262');
          }
      }
  }*/
  if (typeof schema.enum !== 'undefined') {
    schema.enum.should.be.an.Array();
    schema.enum.should.not.be.empty();
    // items only SHOULD be unique
  }
  if (typeof schema.type !== 'undefined') {
    schema.type.should.be.a.String(); // not an array
    schema.type.should.equalOneOf('integer', 'number', 'string', 'boolean', 'object', 'array'); // not null
    if (schema.type === 'array') {
      schema.should.have.property('items');
    }
  }
  if (schema.allOf) {
    schema.allOf.should.be.an.Array();
    schema.allOf.should.not.be.empty();
  }
  if (schema.anyOf) {
    schema.anyOf.should.be.an.Array();
    schema.anyOf.should.not.be.empty();
  }
  if (schema.oneOf) {
    schema.oneOf.should.be.an.Array();
    schema.oneOf.should.not.be.empty();
  }
  if (schema.not) {
    schema.not.should.be.an.Object();
    schema.not.should.not.be.an.Array();
  }
  if (typeof schema.title !== 'undefined') {
    schema.title.should.be.a.String(); //?
  }
  if (typeof schema.description !== 'undefined') {
    schema.description.should.be.a.String();
  }
  if (typeof schema.default !== 'undefined') {
    schema.should.have.property('type');
    let realType = typeof schema.default;
    let schemaType = schema.type;
    if (Array.isArray(schema.default)) realType = 'array';
    if (schemaType === 'integer') schemaType = 'number';
    schemaType.should.equal(realType);
  }
  if (typeof schema.format !== 'undefined') {
    schema.format.should.be.a.String();
    if (
      schema.type &&
      [
        'date-time',
        'email',
        'hostname',
        'ipv4',
        'ipv6',
        'uri',
        'uriref',
        'byte',
        'binary',
        'date',
        'password'
      ].indexOf(schema.format) >= 0
    ) {
      schema.type.should.equal('string');
    }
    if (schema.type && ['int32', 'int64'].indexOf(schema.format) >= 0) {
      if (schema.type !== 'string' && schema.type !== 'number') {
        // common case - googleapis
        schema.type.should.equal('integer');
      }
    }
    if (schema.type && ['float', 'double'].indexOf(schema.format) >= 0) {
      if (schema.type !== 'string') {
        // occasionally seen
        schema.type.should.equal('number');
      }
    }
  }

  if (typeof schema.nullable !== 'undefined') {
    schema.nullable.should.be.a.Boolean();
  }
  if (typeof schema.readOnly !== 'undefined') {
    schema.readOnly.should.be.a.Boolean();
    schema.should.not.have.property('writeOnly');
  }
  if (typeof schema.writeOnly !== 'undefined') {
    schema.writeOnly.should.be.a.Boolean();
    schema.should.not.have.property('readOnly');
  }
  if (typeof schema.deprecated !== 'undefined') {
    schema.deprecated.should.be.a.Boolean();
  }
  if (typeof schema.discriminator !== 'undefined') {
    schema.discriminator.should.be.an.Object();
    schema.discriminator.should.not.be.an.Array();
    schema.discriminator.should.have.property('propertyName');
    //"To avoid redundancy, the discriminator MAY be added to a parent schema definition..."
    //if ((Object.keys(parent).length>0) && !(parent.oneOf || parent.anyOf || parent.allOf)) {
    //    should.fail(false,true,'discriminator requires oneOf, anyOf or allOf in parent schema');
    //}
  }
  if (typeof schema.xml !== 'undefined') {
    schema.xml.should.be.an.Object();
    schema.xml.should.not.be.an.Array();
  }
  // example can be any type

  if (schema.externalDocs) {
    schema.externalDocs.should.have.key('url');
    should.doesNotThrow(function() {
      validateUrl(schema.externalDocs.url, [state.openapi.servers], 'externalDocs', state.options);
    }, 'Invalid externalDocs.url');
  }
  if (prop) state.options.context.pop();
  if (!prop || prop === 'schema') validateSchema(schema, state.openapi, state.options); // top level only
}
