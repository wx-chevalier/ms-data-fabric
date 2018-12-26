INSERT INTO dc_project (id, project_name, create_userid, project_version, project_type_id)
VALUES ('AA2FC46B-A491-4483-A03F-8597047B7EAE',
        'project 1',
        '6b81710b-5677-4743-b141-c6510c35593e',
        'v1.0',
        '63d461f2-4b61-4808-a0c6-73c246b90f07');

INSERT INTO dc_folder (id, project_id, folder_name)
VALUES ('909ED60C-BDCD-4BFE-A31C-268732785737', 'AA2FC46B-A491-4483-A03F-8597047B7EAE', 'folder 1');

INSERT INTO dc_folder (id, project_id, folder_name)
VALUES ('5FB41CB5-C7EE-44F1-999B-8DC3CE87D9B7', 'AA2FC46B-A491-4483-A03F-8597047B7EAE', 'folder 2');
